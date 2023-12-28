package eu.hbb.newyeargame.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.hbb.newyeargame.controller.requests.*;
import eu.hbb.newyeargame.controller.response.RoundInfoResponse;
import eu.hbb.newyeargame.controller.response.SessionStartResponse;
import eu.hbb.newyeargame.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class GameSessionController {


    private GameSessionService gameSessionService;

    @Autowired
    public void setGameSessionService(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @PostMapping("/create/session")
    ResponseEntity<?> startSession(@RequestBody SessionStartRequest request) {
        try {
            String uuid = gameSessionService.createSession(request.getMaxQuestionAnsTime());
            return ResponseEntity.ok(SessionStartResponse
                            .builder()
                            .uuid(uuid)
                            .build());
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/select/game")
    ResponseEntity<?> selectGame(@RequestBody SelectGameRequest request) {
        try {
            gameSessionService.selectGame(request.getUuid(), request.getGameid());
            return ResponseEntity.ok("Success");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/add/team")
    ResponseEntity<?> addTeam(@RequestBody AddTeamRequest request) {
        try {
            gameSessionService.addTeam(request.getUuid(), request.getName(), request.getMembers().get(0), request.getMembers());
            return ResponseEntity.ok("Success");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/start/{id}")
    ResponseEntity<?> startGame(@PathVariable String id) {
        try {
            gameSessionService.startGame(id);
            return ResponseEntity.ok("Success");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/question/select")
    ResponseEntity<?> questionSelected(@RequestBody SelectQuestionRequest request) {
        try {
            gameSessionService.questionSelected(request.getUuid(), request.getQuestionId());
            return ResponseEntity.ok("Success");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/question/answer/{id}")
    ResponseEntity<?> questionAnswered(@PathVariable String id) {
        try {
            gameSessionService.questionAnswered(id);
            return ResponseEntity.ok("Success");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/question/result")
    ResponseEntity<?> questionAnswered(@RequestBody QuestionResultRequest request) {
        try {
            gameSessionService.questionAnswerResult(request.getUuid(), request.getIsTrue());
            return ResponseEntity.ok("Success");
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/get/round/{id}")
    ResponseEntity<?> getRound(@PathVariable String id) {
        try {
            RoundInfoResponse response = gameSessionService.getRound(id);
            return ResponseEntity.ok(response);
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/getByUUID/{uuid}")
    ResponseEntity<?> getSessionById(@PathVariable String uuid) {
        try {
            return ResponseEntity.ok(gameSessionService.getGameSession(uuid));
        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
