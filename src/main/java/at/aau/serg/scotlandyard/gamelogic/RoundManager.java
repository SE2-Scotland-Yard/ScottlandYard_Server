package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.player.*;
import java.util.*;

public class RoundManager {
    private final List<Detective>detectives;
    private final MrX mrX;
    private final List<Player>turnOrder;

    private int currentPlayerTurn = 0;
    private int currentRound = 1;
    private final int maxRounds = 24;

    private final List<Integer> revealRounds = Arrays.asList(3,8,13,18,24);

    public RoundManager(List<Detective> detectives, MrX mrX) {
        this.detectives = detectives;
        this.mrX = mrX;
        this.turnOrder = new ArrayList<>();
        this.turnOrder.add(mrX); //Mr.X starts
        this.turnOrder.addAll(detectives);
    }




}
