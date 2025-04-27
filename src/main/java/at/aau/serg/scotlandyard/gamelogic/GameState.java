package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;

@Component
public class GameState {

    private final Board board;
    private final Map<String, Player> players = new HashMap<>();
    private RoundManager roundManager;
    private int currentRound = 1;
    private final List<Integer> revealRounds = List.of(3, 8, 13, 18, 24); // Sichtbarkeitsrunden
    private final Map<Integer, MrXMove> mrXHistory = new HashMap<>();

    public GameState() {
        this.board = new Board();
    }

    public void initRoundManager(List<Detective>detectives, MrX mrX){ //nicht ideal
        this.roundManager = new RoundManager(detectives, mrX);
    }

    public void addPlayer(String name, Player player) {
        players.put(name, player);
    }

    public List<Integer> getAllowedMoves(String name) {
        Player p = players.get(name);
        //return (p == null) ? List.of() : p.allowedNextMoves(board);
        List<Edge> connections = board.getConnectionsFrom(p.getPosition());

        return connections.stream()
                .filter(edge -> p.getTickets().hasTicket(edge.getTicket()))
                .map(Edge::getTo)
                .filter(position -> !isPositionOccupied(position) || p instanceof MrX)
                .collect(Collectors.toList());
    }

    public boolean movePlayer(String name, int to, Ticket ticket) {
        Player p = players.get(name);
        if (p instanceof MrX mrX) {
            if (mrX.isValidMove(to, ticket, board)) {
                mrX.move(to, ticket, board);
                mrXHistory.put(currentRound, new MrXMove(to, ticket));
                currentRound++;
                return true;
            }
        }
        if (p != null && p.isValidMove(to, ticket, board)) {
            p.move(to, ticket, board);
            currentRound++;
            return true;
        }
        if (p instanceof Detective) {
            for (Player other : players.values()) {
                if (other != p && other instanceof Detective && other.getPosition() == to) {
                    return false; // Ziel ist von anderem Detective besetzt
                }
            }
        }

        return false;
    }

    public Board getBoard() {
        return board;
    }
    public List<Integer> getRevealRounds(){return List.copyOf(revealRounds);}

    public Map<String, Player> getAllPlayers() {
        return players;
    }

    //Winning Condition
    public enum Winner{ MR_X, DETECTIVE, NONE};

    public Winner getWinner(){
        if(!roundManager.isGameOver()){
            return Winner.NONE; //Game still running
        }
        if(roundManager.isMrXCaptured()){
            return Winner.DETECTIVE;
        }
        return Winner.MR_X;
    }


}
