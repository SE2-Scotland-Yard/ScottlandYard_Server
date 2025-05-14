package at.aau.serg.scotlandyard.gamelogic;


import at.aau.serg.scotlandyard.dto.GameMapper;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class GameState {
    private final SimpMessagingTemplate messaging;
    private final String gameId;
    private final Board board;
    private final Map<String, Player> players = new HashMap<>();
    private RoundManager roundManager;
    private int currentRound = 1;
    private final List<Integer> revealRounds = List.of(3, 8, 13, 18, 24); // Sichtbarkeitsrunden
    private final Map<Integer, MrXMove> mrXHistory = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(GameState.class);




    public GameState(String gameId, SimpMessagingTemplate messaging) {
        this.board = new Board();
        this.gameId = gameId;
        this.messaging = messaging;
    }

    public void initRoundManager(List<Detective>detectives, MrX mrX){ //nicht ideal
        this.roundManager = new RoundManager(detectives, mrX);
    }

    public boolean canMove(String playerName, int target, Ticket ticket) {
        Player player = players.get(playerName);
        return player != null
                && getAllowedMoves(playerName).contains(target)
                && player.getTickets().hasTicket(ticket)
                && (player instanceof MrX || !isPositionOccupied(target));
    }
    public void addPlayer(String name, Player player) {
        players.put(name, player);
    }

    public List<Map.Entry<Integer, Ticket>> getAllowedMoves(String name) {
        Player p = players.get(name);
        if (p == null) {
            return List.of();
        }

        List<Edge> connections = board.getConnectionsFrom(p.getPosition());

        return connections.stream()
                .filter(edge -> p.getTickets().hasTicket(edge.getTicket()))
                .filter(edge -> !isPositionOccupied(edge.getTo()) || p instanceof MrX)
                .map(edge -> Map.entry(edge.getTo(), edge.getTicket()))
                .toList();
    }

    public Integer getMrXPosition(String name) {
        int position=0;
        Player p = players.get(name);
        if (p == null) {
            return 0;
        }

        position = p.getPosition();
        return position;
    }

    public boolean movePlayer(String name, int to, Ticket ticket) {
        Player p = players.get(name);
        if (p instanceof MrX mrX && mrX.isValidMove(to, ticket, board)) {
                mrX.move(to, ticket, board);
                mrXHistory.put(currentRound, new MrXMove(to, ticket));
                currentRound++;
                roundManager.nextTurn();

            String nextPlayer = getCurrentPlayerName();
            System.out.println("➡️ currentRound: " + currentRound + ", nextPlayer: " + nextPlayer);
            messaging.convertAndSend("/topic/game/" + gameId,
                    GameMapper.mapToGameUpdate(
                            gameId,
                            getAllPlayers(),
                            getCurrentPlayerName()
                    )
            );
                return true;
        }
        if (p != null && p.isValidMove(to, ticket, board)) {
            p.move(to, ticket, board);
            currentRound++;
            roundManager.nextTurn();

            String nextPlayer = getCurrentPlayerName();
            System.out.println("➡️ currentRound: " + currentRound + ", nextPlayer: " + nextPlayer);
            messaging.convertAndSend("/topic/game/" + gameId,
                    GameMapper.mapToGameUpdate(
                            gameId,
                            getAllPlayers(),
                            getCurrentPlayerName()
                    )
            );
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

    public String getCurrentPlayerName() {
        if (roundManager == null || roundManager.getCurrentPlayer() == null) {
            return null;
        }
        return roundManager.getCurrentPlayer().getName();
    }



    //Winning Condition
    public enum Winner{ MR_X, DETECTIVE, NONE}

    public Winner getWinner(){
        if(!roundManager.isGameOver()){
            return Winner.NONE; //Game still running
        }
        if(roundManager.isMrXCaptured()){
            return Winner.DETECTIVE;
        }
        return Winner.MR_X;
    }

    public boolean isPositionOccupied(int position) {
        return players.values().stream()
                .anyMatch(p -> p.getPosition() == position);
    }

    public boolean moveMrXDouble(String name, int firstTo, Ticket firstTicket, int secondTo, Ticket secondTicket) {
        Player p = players.get(name);
        if (p instanceof MrX mrX) {
            try {
                mrX.moveDouble(firstTo, firstTicket, secondTo, secondTicket, board);
                mrXHistory.put(currentRound, new MrXMove(firstTo, firstTicket));
                mrXHistory.put(currentRound + 1, new MrXMove(secondTo, secondTicket));
                currentRound+=2;
                return true;
            } catch (IllegalArgumentException e) {
                logger.info("Ungültiger Doppelzug von MrX: {}" , e.getMessage());
            }
        }
        return false;
    }

    public String getVisibleMrXPosition() {
        MrX mrX = null;
        for (Player p : players.values()) {
            if (p instanceof MrX mrx) {
                mrX = mrx;
                break;
            }
        }
        if (mrX == null) return "MrX nicht im Spiel";

        if (revealRounds.contains(currentRound - 1)) {
            return String.valueOf(mrX.getPosition()); // letzte sichtbare Position
        } else {
            return "?";
        }
    }

    public List<String> getMrXMoveHistory() {
        List<String> history = new ArrayList<>();

        for (int i = 1; i <= currentRound - 1; i++) {
            MrXMove move = mrXHistory.get(i);
            if (move == null) continue;

            String pos = revealRounds.contains(i) ? String.valueOf(move.getPosition()) : "?";
            String ticket = move.getTicket().name();

            history.add("Runde " + i + ": " + pos + " (" + ticket + ")");
        }

        return history;
    }

}

class MrXMove {
    private final int position;
    private final Ticket ticket;

    public MrXMove(int position, Ticket ticket) {
        this.position = position;
        this.ticket = ticket;
    }

    public int getPosition() {
        return position;
    }

    public Ticket getTicket() {
        return ticket;
    }
}



