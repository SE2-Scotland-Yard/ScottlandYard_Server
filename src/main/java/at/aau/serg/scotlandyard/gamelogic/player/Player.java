package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.PlayerTickets;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;

import java.util.*;

public abstract class Player {
   protected final PlayerTickets tickets;
   protected int pos;

    public Player(PlayerTickets tickets) {
        this.tickets = tickets;
        this.pos = new Random().nextInt(199)+1;

    }

    public boolean isValidMove(int to, Ticket ticket, Board board) {
        if (!tickets.hasTicket(ticket)) {
            return false;
        }

        return false; //Todo Implement
    }
    public void move(int to, Ticket ticket) {
        if (isValidMove(to, ticket, new Board())) {
            tickets.useTicket(ticket);
            pos = to;
        } else {
            throw new IllegalArgumentException("Invalid move!");
        }
    }

    public PlayerTickets getTickets() {
        return tickets;
    }

    public int getPosition() {
        return pos;
    }

    public List<Integer> allowedNextMoves(Board board) {
        List<Integer> allowedMoves = new ArrayList<>();
        for (Edge edge : board.getConnectionsFrom(this.pos)) {
            Ticket requiredTicket = transportToTicket(edge.getTicket());
            if (tickets.hasTicket(requiredTicket)) {
                allowedMoves.add(edge.getTo());
            }
        }
        return allowedMoves;
    }

    private Ticket transportToTicket(Ticket ticket) {
       return ticket;
    }
}
