package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.player.tickets.PlayerTickets;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;

import java.util.HashMap;
import java.util.Map;

public class Detective extends Player {

    public Detective() {
        super(initializeTickets());

    }

    private static PlayerTickets initializeTickets() {
        Map<Ticket, Integer> initialTickets = new HashMap<>();
        initialTickets.put(Ticket.TAXI, 10);
        initialTickets.put(Ticket.BUS, 8);
        initialTickets.put(Ticket.UNDERGROUND, 4);
        return new PlayerTickets(initialTickets);
    }
}
