package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.player.tickets.PlayerTickets;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;

import java.util.HashMap;
import java.util.Map;

public class MrX extends Player {
    private int doubleTicket;

    public MrX() {
        super(initializeTickets());
        doubleTicket = 2;
    }
    private static PlayerTickets initializeTickets() {
        Map<Ticket, Integer> initialTickets = new HashMap<>();

        initialTickets.put(Ticket.TAXI, 4);
        initialTickets.put(Ticket.BUS, 3);
        initialTickets.put(Ticket.TRAIN, 3);
        initialTickets.put(Ticket.BLACK, 5);
        initialTickets.put(Ticket.DOUBLE, 2);  // Falls Doppelzüge erlaubt sind

        return new PlayerTickets(initialTickets);
    }

    public boolean canUseDoubleTicket() {
        return doubleTicket > 0;
    }

    public void useDoubleTicket() {
        if (!canUseDoubleTicket()) {
            throw new IllegalStateException("No DOUBLE tickets left!");
        }
        doubleTicket--;
    }
}
