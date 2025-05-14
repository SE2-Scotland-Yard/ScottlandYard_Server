package at.aau.serg.scotlandyard.dto;


import javax.swing.plaf.IconUIResource;
import java.util.List;
import java.util.Map;

public class GameUpdate {
    private String gameId;
    private Map<String, Integer> playerPositions;
    private String currentPlayer;


    public GameUpdate(String gameId, Map<String, Integer> playerPositions, String currentPlayer) {
        this.gameId = gameId;
        this.playerPositions = playerPositions;
        this.currentPlayer = currentPlayer;

    }

    public String getGameId() {
        return gameId;
    }

    public String getCurrentPlayer(){

        return currentPlayer;
    }

    public Map<String, Integer> getPlayerPositions() {
        return playerPositions;
    }
}

