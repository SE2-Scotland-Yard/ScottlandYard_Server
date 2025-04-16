package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
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
        return (p == null) ? List.of() : p.allowedNextMoves(board);
    }

    public boolean movePlayer(String name, int to, Ticket ticket) {
        Player p = players.get(name);
        if (p != null && p.isValidMove(to, ticket, board)) {
            p.move(to, ticket);
            return true;
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }
}
