package eu.hbb.newyeargame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.hbb.newyeargame.models.GameSession;
import eu.hbb.newyeargame.service.GameSessionService;
import eu.hbb.newyeargame.service.InitializerService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class NewyeargameApplicationTests {


    private GameSessionService gameSessionService;

    @Test
    void contextLoads() {
        String uuid = gameSessionService.createSession(10);
        GameSession gameSession = gameSessionService.getGameSession(uuid);
        new Thread(() -> {
            gameSessionService.selectGame(uuid, 1);
            gameSessionService.addTeam(
                    uuid,
                    "Команда 1",
                    "Максим",
                    Lists.list("Максим", "Игорь", "Алеся")
            );
            gameSessionService.addTeam(
                    uuid,
                    "Команда 2",
                    "Оля",
                    Lists.list("Оля", "Витя", "Катя")
            );
            gameSessionService.startGame(uuid);
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gameSessionService.questionSelected(uuid, 1);
            gameSessionService.questionAnswered(uuid);
            gameSessionService.questionAnswerResult(uuid, true);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                System.out.println(objectMapper.writeValueAsString(gameSessionService.getGameSession(uuid)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Autowired
    public void setGameSessionService(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }
}
