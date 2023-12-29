package eu.hbb.newyeargame.controller;

import eu.hbb.newyeargame.models.Game;
import eu.hbb.newyeargame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private GameService gameService;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/get/byId/{id}")
    ResponseEntity<?> getGameById(@PathVariable String id) {
        long gameid = Long.parseLong(id);
        Game game = gameService.getGameById(gameid);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/get/games/all")
    ResponseEntity<?> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }
}
