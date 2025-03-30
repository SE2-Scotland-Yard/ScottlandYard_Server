package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Transport;

public class Detective extends Player {

    public Detective() {
        super();
        ticketsLeft.put(Transport.TAXI,11);
        ticketsLeft.put(Transport.BUS,8);
        ticketsLeft.put(Transport.TRAIN,4);
        ticketsLeft.put(Transport.BOAT,0);
    }
}
