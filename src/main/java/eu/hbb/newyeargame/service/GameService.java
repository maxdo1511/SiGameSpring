package eu.hbb.newyeargame.service;

import eu.hbb.newyeargame.entity.*;
import eu.hbb.newyeargame.repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {


    private GameRepository gameRepository;
    private UserRepository userRepository;
    private RoundRepository roundRepository;
    private QuestionRepository questionRepository;
    private ThemeRepository themeRepository;

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoundRepository(RoundRepository roundRepository) {
        this.roundRepository = roundRepository;
    }

    @Autowired
    public void setQuestionRepository(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Autowired
    public void setThemeRepository(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Game parseGameJSONToGame(GameJSON gameJSON) {
        Map<RoundEntity, List<ThemeEntity>> themes = new HashMap<>();
        Map<ThemeEntity, List<QuestionEntity>> questions = new HashMap<>();
        Map<Long, List<QuestionEntity>> questionsIds = new HashMap<>();
        Map<Long, List<ThemeEntity>> themesIds = new HashMap<>();
        for (QuestionEntity questionEntity : gameJSON.getQuestions()) {
            if (!questionsIds.containsKey(questionEntity.getThemeid())) {
                questionsIds.put(questionEntity.getThemeid(), new ArrayList<>());
            }
            questionsIds.get(questionEntity.getThemeid()).add(questionEntity);
        }
        for (ThemeEntity themeEntity : gameJSON.getThemes()) {
            if (!themesIds.containsKey(themeEntity.getRoundid())) {
                themesIds.put(themeEntity.getRoundid(), new ArrayList<>());
            }
            themesIds.get(themeEntity.getRoundid()).add(themeEntity);
        }
        for (RoundEntity round : gameJSON.getRounds()) {
            themes.put(round, themesIds.get(round.getId()));
        }
        for (ThemeEntity theme : gameJSON.getThemes()) {
            questions.put(theme, questionsIds.get(theme.getId()));
        }
        return Game.builder()
                .game(gameJSON.getGame())
                .themes(themes)
                .questions(questions)
                .rounds(gameJSON.getRounds())
                .build();
    }

    @Transactional
    public Game createGameById(long id) {
        GameEntity gameEntity = gameRepository.findById(id).orElseThrow();
        List<RoundEntity> roundEntities = roundRepository.findAllByGameid(gameEntity.getId()).orElseThrow();
        Map<RoundEntity, List<ThemeEntity>> themes = new HashMap<>();
        Map<ThemeEntity, List<QuestionEntity>> questions = new HashMap<>();
        for (RoundEntity round : roundEntities) {
            List<ThemeEntity> themeEntities = themeRepository.findAllByRoundid(round.getId()).orElseThrow();
            themes.put(round, themeEntities);
            for (ThemeEntity theme : themeEntities) {
                List<QuestionEntity> questionEntities = questionRepository.findAllByThemeid(theme.getId()).orElseThrow();
                questions.put(theme, questionEntities);
            }
        }
        return Game.builder().
                game(gameEntity).
                rounds(roundEntities).
                themes(themes).
                questions(questions).
                build();
    }
}
