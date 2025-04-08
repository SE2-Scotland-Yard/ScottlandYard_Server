package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.board.Transport;

import java.util.*;

public abstract class Player {
    public Map<Transport, Integer> ticketsLeft;
    public int pos;

    public Player() {
        Random rand = new Random();
        pos = rand.nextInt(199);
        ticketsLeft = new HashMap<>();
    }

    public void move(Transport ticket, int to) {
        //todo Implement
    }
    public boolean isValidMove(int to){
        return false; //Todo Implement
    }

    public List<Integer> allowedNextMoves(Board board) {
        List<Integer> allowed = new ArrayList<>();
        for (Edge edge : board.getConnectionsFrom(this.pos)) {
            Transport transport = edge.getTransport();
            if (ticketsLeft.getOrDefault(transport, 0) > 0) {
                allowed.add(edge.getTo());
            }
        }
        return allowed;
    }

}
