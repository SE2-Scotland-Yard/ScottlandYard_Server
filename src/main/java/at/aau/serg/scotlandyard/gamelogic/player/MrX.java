package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.player.tickets.PlayerTickets;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;

import java.util.HashMap;
import java.util.Map;

public class MrX extends Player {
    
    public MrX() {
        super(initializeTickets());

    }
    private static PlayerTickets initializeTickets() {
        Map<Ticket, Integer> initialTickets = new HashMap<>();

        initialTickets.put(Ticket.taxi, 4);
        initialTickets.put(Ticket.bus, 3);
        initialTickets.put(Ticket.underground, 3);
        initialTickets.put(Ticket.BLACK, 5);
        initialTickets.put(Ticket.DOUBLE, 2);

        return new PlayerTickets(initialTickets);
    }


}
