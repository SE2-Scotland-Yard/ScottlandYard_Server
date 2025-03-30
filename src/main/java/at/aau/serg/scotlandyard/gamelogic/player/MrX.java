package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Transport;

public class MrX extends Player {
    int doubleTickets;

    public MrX() {
        super();
        ticketsLeft.put(Transport.TAXI,0);
        ticketsLeft.put(Transport.BUS,0);
        ticketsLeft.put(Transport.TRAIN,0);
        ticketsLeft.put(Transport.BOAT,5);
    }
}
