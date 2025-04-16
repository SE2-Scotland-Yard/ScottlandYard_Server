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

    public boolean isValidMove(int to, Ticket ticket) {
        if (!tickets.hasTicket(ticket)) {
            return false;
        }

        return false; //Todo Implement
    }



}
