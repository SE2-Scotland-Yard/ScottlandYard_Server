package at.aau.serg.scotlandyard.controller;

import at.aau.serg.scotlandyard.dto.GameOverviewDTO;
import at.aau.serg.scotlandyard.gamelogic.GameManager;
import at.aau.serg.scotlandyard.gamelogic.GameState;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {

    private static final String GAME_NOT_FOUND = "Spiel nicht gefunden";
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
                       @RequestParam int to,
                       @RequestParam Ticket ticket) {
        GameState game = gameManager.getGame(gameId);
        if (game == null) return GAME_NOT_FOUND;
        if (!game.movePlayer(name, to, ticket)) return "Ungültiger Zug!";
        game.movePlayer(name, to, ticket);

        if(game.getWinner() != GameState.Winner.NONE){
            return getWinner(gameId);
        }

        return "Spieler " + name + " bewegt sich zu " + to + " in Spiel " + gameId;
    }

    @PostMapping("/moveDouble")
    public String moveDouble(@RequestParam String gameId,
                             @RequestParam String name,
                             @RequestParam int firstTo,
                             @RequestParam Ticket firstTicket,
                             @RequestParam int secondTo,
                             @RequestParam Ticket secondTicket) {
        GameState game = gameManager.getGame(gameId);
        if (game == null) return GAME_NOT_FOUND;
        if (!game.moveMrXDouble(name, firstTo, firstTicket, secondTo, secondTicket)) {
            return "Ungültiger Doppelzug!";
        }
        return "MrX machte einen Doppelzug: " + firstTo + " → " + secondTo;
    }

    @GetMapping("/mrXposition")
    public String getMrXPosition(@RequestParam String gameId) {
        GameState game = gameManager.getGame(gameId);
        return (game != null) ? game.getVisibleMrXPosition() : GAME_NOT_FOUND;
    }

    @GetMapping("/mrXhistory")
    public List<String> getMrXHistory(@RequestParam String gameId) {
        GameState game = gameManager.getGame(gameId);
        return (game != null) ? game.getMrXMoveHistory() : List.of(GAME_NOT_FOUND);
    }

    @GetMapping("/all")
    public List<GameOverviewDTO> getAllGames() {
        return gameManager.getAllGameIds().stream()
                .map(id -> {
                    GameState game = gameManager.getGame(id);
                    Map<String, String> players = new HashMap<>();
                    for (var entry : game.getAllPlayers().entrySet()) {
                        players.put(entry.getKey(), entry.getValue().getClass().getSimpleName());
                    }
                    return new GameOverviewDTO(id, players);
                })
                .toList();
    }

    @GetMapping("/winner")
    public String getWinner(@RequestParam String gameId) {
        GameState game = gameManager.getGame(gameId);
        if (game == null) return GAME_NOT_FOUND;
        switch (game.getWinner()) {
            case MR_X:
                return "Mr.X hat gewonnen!";
            case DETECTIVE:
                return "Detektive haben gewonnen!";
            default:
                return "Spiel läuft noch.";
        }
    }
}
