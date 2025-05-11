package at.aau.serg.scotlandyard.dto;

import at.aau.serg.scotlandyard.gamelogic.player.Player;

import java.util.List;
import java.util.Map;

public class GameUpdate {
    private String gameId;

    private Map<Player,Integer> playerPositions;


    public GameUpdate(String gameId, Map<Player,Integer> playerPositions) {
        this.gameId = gameId;
         this.playerPositions = playerPositions;

    }


}
