package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
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

    public void moveDouble(int firstTo, Ticket firstTicket, int secondTo, Ticket secondTicket, Board board) {
        if (!tickets.hasTicket(Ticket.DOUBLE)) {
            throw new IllegalArgumentException("Kein DOUBLE-Ticket verfügbar!");
        }

        // Prüfe beide Züge
        if (!isValidMove(firstTo, firstTicket, board)) {
            throw new IllegalArgumentException("Erster Zug ungültig!");
        }

        // Simuliere erste Bewegung temporär, um zweiten Zug zu validieren
        int originalPos = this.pos;
        this.pos = firstTo;

        boolean validSecond = isValidMove(secondTo, secondTicket, board);
        this.pos = originalPos; // Position zurücksetzen

        if (!validSecond) {
            throw new IllegalArgumentException("Zweiter Zug ungültig!");
        }

        // Zug ist gültig → Tickets verwenden und Position setzen
        tickets.useTicket(Ticket.DOUBLE);
        tickets.useTicket(firstTicket);
        this.pos = firstTo;

        tickets.useTicket(secondTicket);
        this.pos = secondTo;

        System.out.println("MrX machte einen Doppelzug: " + firstTo + " → " + secondTo);
    }
}
