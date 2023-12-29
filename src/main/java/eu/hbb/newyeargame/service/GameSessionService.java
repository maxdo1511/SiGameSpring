package eu.hbb.newyeargame.service;

import eu.hbb.newyeargame.configuration.CustomProperty;
import eu.hbb.newyeargame.controller.response.RoundInfoResponse;
import eu.hbb.newyeargame.entity.QuestionEntity;
import eu.hbb.newyeargame.entity.RoundEntity;
import eu.hbb.newyeargame.entity.ThemeEntity;
import eu.hbb.newyeargame.enums.GameStage;
import eu.hbb.newyeargame.models.Game;
import eu.hbb.newyeargame.models.GameSession;
import eu.hbb.newyeargame.models.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameSessionService {

    private Map<String, GameSession> sessions = new HashMap<>();

    private GameService gameService;
    @Autowired
    private CustomProperty customProperty;


    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public GameSession getGameSession(String uuid) {
        return getGameSessionSafe(uuid);
    }

    /**
     * @return uuid игры, чтобы управлять сохранить в localstorage для дальнейшего общения
     */
    public String createSession(int maxQuestionAnsTime) {
        String uuid = UUID.randomUUID().toString();
        sessions.put(uuid, new GameSession(
                uuid,
                GameStage.GAME_SELECTION,
                new HashMap<>(),
                new ArrayDeque<>(),
                maxQuestionAnsTime,
                -1,
                -1,
                new LinkedHashMap<>(),
                null,
                null
        ));
        return uuid;
    }

    public void selectGame(String uuid, long gameid) {
        GameSession gameSession = getGameSessionSafe(uuid);
        if (!gameService.existsGameById(gameid)) {
            throw new RuntimeException("No game with id " + gameid);
        }
        GameStage.GAME_SELECTION.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        gameSession.setGame(gameid);
        gameSession.setStage(GameStage.TEAMS_INIT);
    }

    public void addTeam(String uuid, String name, String owner, List<String> members) {
        GameSession gameSession = getGameSessionSafe(uuid);
        GameStage.TEAMS_INIT.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        if (gameSession.getTeams().values().stream().anyMatch(s -> s.getName().equalsIgnoreCase(name))) {
            throw new RuntimeException(String.format("Team with name %s already exists", name));
        }
        String teamUUID = UUID.randomUUID().toString();
        Team team = Team.builder()
                .uuid(teamUUID)
                .name(name)
                .owner(owner)
                .members(members)
                .score(0)
                .ansTimeHistory(new ArrayList<>())
                .scoreHistory(new ArrayList<>())
                .build();
        gameSession.getTeams().put(teamUUID, team);
    }

    public void startGame(String uuid) {
        GameSession gameSession = getGameSessionSafe(uuid);
        GameStage.TEAMS_INIT.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        if (gameSession.getTeams().size() < 2) {
            throw new RuntimeException(String.format("You should create more teams (min count = 2, but now %s)", gameSession.getTeams().size()));
        }
        gameSession.getTeamQueue().clear();
        List<String> queue = new ArrayList<>(gameSession.getTeams().keySet().stream().toList());
        Collections.reverse(queue);
        gameSession.getTeamQueue().addAll(queue);

        startNextRound(gameSession);
    }

    public void questionSelected(String sessionUUID, long questionID) {
        GameSession gameSession = getGameSessionSafe(sessionUUID);
        GameStage.QUESTION_SELECTION.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        for (QuestionEntity questionEntity : gameSession.getQuestions().keySet()) {
            if (questionEntity.getId() == questionID) {
                if (!gameSession.getQuestions().get(questionEntity)) {
                    throw new RuntimeException("Question already had been ans");
                }
                gameSession.setStage(GameStage.QUESTION);
                gameSession.setCurrentQuestion(questionEntity);
                break;
            }
        }
    }

    public void questionAnswered(String sessionUUID) {
        GameSession gameSession = getGameSessionSafe(sessionUUID);
        GameStage.QUESTION.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        gameSession.setStage(GameStage.QUESTION_ANS);
    }

    public void questionAnswerResult(String sessionUUID, boolean isAnsTrue) {
        GameSession gameSession = getGameSessionSafe(sessionUUID);
        GameStage.QUESTION_ANS.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        setCurrentQuestionClose(gameSession);
        addPointsToCurrentTeam(gameSession, isAnsTrue);
        switchToNextTeam(gameSession);

        gameSession.setCurrentQuestion(null);

        if (checkNextRound(gameSession)) {
            startNextRound(gameSession);
            return;
        }

        gameSession.setStage(GameStage.QUESTION_SELECTION);
    }

    public RoundInfoResponse getRound(String uuid) {
        GameSession gameSession = getGameSessionSafe(uuid);
        if (!GameStage.ROUND_START.stageMoreThan(gameSession.getStage())) {
            throw new RuntimeException("Incorrect stage " + gameSession.getStage());
        }

        Map<Long, Boolean> statuses = new HashMap<>();
        for (QuestionEntity questionEntity : gameSession.getQuestions().keySet()) {
            statuses.put(questionEntity.getId(), gameSession.getQuestions().get(questionEntity));
        }

        // Game game = gameService.getGameById(gameSession.getGame());
        return RoundInfoResponse.builder()
                .ansTeam(gameSession.getTeamQueue().peek())
                .name(gameSession.getCurrentRound().getName())
                .themes(gameService.getGameById(gameSession.getGame()).getThemes().get(gameSession.getCurrentRound()).stream().map(ThemeEntity::getName).toList())
                .questionEntities(gameSession.getQuestions().keySet().stream().toList())
                .questionStatuses(statuses)
                .build();
    }

    public Team getCurrentTeam(String uuid) {
        GameSession gameSession = getGameSessionSafe(uuid);
        if (!GameStage.ROUND_START.stageMoreThan(gameSession.getStage())) {
            throw new RuntimeException("Incorrect stage " + gameSession.getStage());
        }

        String teamUUID = gameSession.getTeamQueue().peek();
        Team team = gameSession.getTeams().get(teamUUID);
        if (team == null) {
            throw new RuntimeException("No teams in queue");
        }

        return team;
    }

    private void fillQuestions(Game game, GameSession gameSession) {
        gameSession.getQuestions().clear();
        for (ThemeEntity theme : game.getThemes().get(gameSession.getCurrentRound())) {
            for (QuestionEntity questionEntity : game.getQuestions().get(theme)) {
                gameSession.getQuestions().put(questionEntity, true);
            }
        }
    }

    private void startNextRound(GameSession gameSession) {
        Game game = gameService.getGameById(gameSession.getGame());
        int roundID = -1;
        if (gameSession.getCurrentRound() != null) {
            RoundEntity round = gameSession.getCurrentRound();
            for (int i = 0; i < game.getRounds().size() - 1; i++) {
                if (round.equals(game.getRounds().get(i))) {
                    roundID = i + 1;
                }
            }
        }else {
            roundID = 0;
        }

        if (roundID == -1) {
            endGame(gameSession);
        }

        gameSession.setCurrentRound(game.getRounds().get(roundID));
        gameSession.setStage(GameStage.ROUND_START);

        fillQuestions(game, gameSession);

        new Thread(() -> {
            try {
                Thread.sleep(customProperty.getRoundStartTime() * 1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            roundStarted(gameSession);
        }).start();
    }

    private void endGame(GameSession gameSession) {
        gameSession.setStage(GameStage.RESULT);
        //TODO fill stats
    }

    private Team getWinner(String uuid) {
        GameSession gameSession = getGameSessionSafe(uuid);
        GameStage.RESULT.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        return gameSession.getTeams().values().stream().sorted(Comparator.comparingInt(Team::getScore)).toList().getFirst();
    }

    private void roundStarted(GameSession gameSession) {
        GameStage.ROUND_START.checkPermissionAndThrowExceptionIfNotValid(gameSession.getStage());

        gameSession.setStage(GameStage.QUESTION_SELECTION);
    }

    private GameSession getGameSessionSafe(String uuid) {
        GameSession gameSession = sessions.get(uuid);
        if (gameSession == null) {
            throw new RuntimeException("No session with uuid " + uuid);
        }
        return gameSession;
    }

    private void switchToNextTeam(GameSession gameSession) {
        String team = gameSession.getTeamQueue().poll();
        gameSession.getTeamQueue().add(team);
    }

    private void addPointsToCurrentTeam(GameSession gameSession, boolean isMastAdd) {
        int factor = 1;
        if (!isMastAdd) factor = -1;
        Team team = gameSession.getTeams().get(gameSession.getTeamQueue().peek());
        team.setScore(team.getScore() + gameSession.getCurrentQuestion().getCost() * factor);
    }

    private void setCurrentQuestionClose(GameSession gameSession) {
        gameSession.getQuestions().put(gameSession.getCurrentQuestion(), false);
    }

    private boolean checkNextRound(GameSession gameSession) {
        long c = gameSession.getQuestions().values().stream().filter(v -> !v).count();
        long s = gameSession.getQuestions().size();
        return c == s;
    }

}
