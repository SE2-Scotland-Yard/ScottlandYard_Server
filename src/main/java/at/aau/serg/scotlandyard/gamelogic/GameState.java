package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GameState {

    private final Board board;
    private final Map<String, Player> players = new HashMap<>();

    public GameState() {
        this.board = new Board();
    }

    public void addPlayer(String name, Player player) {
        players.put(name, player);
    }

    public List<Integer> getAllowedMoves(String name) {
        Player p = players.get(name);
        if (p == null) return List.of();
        return p.allowedNextMoves(board); //
    }

    public void movePlayer(String name, int to) {
        Player p = players.get(name);
        if (p != null && p.isValidMove(to)) {
            //todo implement
        }
    }

    public Board getBoard() {
        return board;
    }

    public Map<String, Player> getAllPlayers() {
        return players;
    }

}
