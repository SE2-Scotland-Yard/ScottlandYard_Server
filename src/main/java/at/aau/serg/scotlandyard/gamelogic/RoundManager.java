package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.player.*;


import java.util.*;


public class RoundManager {
    private final List<Detective>detectives;
    private final MrX mrX;
    private final List<Player>turnOrder;

    private int currentPlayerTurn = 0; //index that indicates which player is next
    private int currentRound = 1;
    private static final int MAXROUNDS = 24;

    private final List<Integer> revealRounds = Arrays.asList(3,8,13,18,24); //for Mr.X

    public RoundManager(List<Detective> detectives, MrX mrX) {
        this.detectives = detectives;
        this.mrX = mrX;
        this.turnOrder = new ArrayList<>();
        this.turnOrder.add(mrX); //Mr.X starts
        this.turnOrder.addAll(detectives);
    }

    public Player getCurrentPlayer() {
        return turnOrder.get(currentPlayerTurn);
    }

    public void nextTurn(){
        currentPlayerTurn++;

        if(currentPlayerTurn >= turnOrder.size()){
            currentPlayerTurn = 0;
            currentRound++;
        }
    }

    public boolean isMrXVisible() {
        return revealRounds.contains(currentRound);
    }

    public boolean isMrXCaptured(){
        for(Detective detective : detectives){
            if(detective.getPosition() == mrX.getPosition()){
                return true;
            }
        }
        return false;
    }

    public boolean isGameOver(){
        return currentRound > MAXROUNDS || isMrXCaptured();
    }



    public int getCurrentRound() {
        return currentRound;
    }

    public List<Detective> getDetectives() {
        return detectives;
    }

    public MrX getMrX(){
        return mrX;
    }


}
