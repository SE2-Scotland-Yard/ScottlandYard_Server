package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Transport;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class Player {
    Map<Transport, Integer> ticketsLeft;
    int pos;

    public Player() {
        Random rand = new Random();
        pos = rand.nextInt(199);
        ticketsLeft = new HashMap<>();
    }

    public void move(Transport ticket, int to) {
        //todo Implement
    }
    public boolean isValidMove(int to){
        return false; //Todo Implement
    }
}
