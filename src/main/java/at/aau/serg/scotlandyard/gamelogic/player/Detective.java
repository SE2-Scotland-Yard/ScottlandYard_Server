package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Transport;

public class Detective extends Player {

    public Detective() {
        super();
        ticketsLeft.put(Transport.taxi,11);
        ticketsLeft.put(Transport.bus,8);
        ticketsLeft.put(Transport.underground,4);
        ticketsLeft.put(Transport.BOAT,0);
    }
}
