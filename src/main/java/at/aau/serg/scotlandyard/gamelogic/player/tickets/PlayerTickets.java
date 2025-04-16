package at.aau.serg.scotlandyard.gamelogic.player.tickets;

import java.util.HashMap;
import java.util.Map;

public class PlayerTickets {
    private final Map<Ticket, Integer> tickets;

    public PlayerTickets(Map<Ticket, Integer> initialTickets) {
        this.tickets = new HashMap<>(initialTickets);
    }

    public boolean hasTicket(Ticket ticket) {
        return tickets.getOrDefault(ticket, 0) > 0;
    }
}


