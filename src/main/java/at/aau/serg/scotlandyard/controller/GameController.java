package at.aau.serg.scotlandyard.controller;

import at.aau.serg.scotlandyard.gamelogic.GameManager;
import at.aau.serg.scotlandyard.gamelogic.GameState;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameManager gameManager;

    public GameController(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @GetMapping("/allowedMoves")
    public List<Integer> getMoves(@RequestParam String gameId,
                                  @RequestParam String name) {
        GameState game = gameManager.getGame(gameId);
        if (game == null) return List.of();
        return game.getAllowedMoves(name);
    }

    @PostMapping("/move")
    public String move(@RequestParam String gameId,
                       @RequestParam String name,
                       @RequestParam int to) {
        GameState game = gameManager.getGame(gameId);
        if (game == null) return "Spiel nicht gefunden!";
        game.movePlayer(name, to);
        return "Spieler " + name + " bewegt sich zu " + to + " in Spiel " + gameId;
    }
}
