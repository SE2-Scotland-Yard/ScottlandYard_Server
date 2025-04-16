package at.aau.serg.scotlandyard.gamelogic.player;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.PlayerTickets;

import java.util.*;

public abstract class Player {
   private final PlayerTickets tickets;
    public int pos;

    public Player(PlayerTickets tickets) {
        this.tickets = tickets;
        Random rand = new Random();
        pos = rand.nextInt(199);

    }


    public boolean isValidMove(int to){
        return false; //Todo Implement
    }



}
