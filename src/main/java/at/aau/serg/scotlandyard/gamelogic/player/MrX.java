package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Transport;

public class MrX extends Player {
    int doubleTickets;

    public MrX() {
        super();
        ticketsLeft.put(Transport.taxi,0);
        ticketsLeft.put(Transport.bus,0);
        ticketsLeft.put(Transport.underground,0);
        ticketsLeft.put(Transport.BOAT,5);
    }
}
