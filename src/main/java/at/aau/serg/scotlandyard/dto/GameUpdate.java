package at.aau.serg.scotlandyard.dto;


import java.util.Map;

public class GameUpdate {
    private String gameId;
    private Map<String, Integer> playerPositions;
    private String currentPlayer;

    public GameUpdate(String gameId, Map<String, Integer> playerPositions) {
        this.gameId = gameId;
        this.playerPositions = playerPositions;
    }

    public String getGameId() {
        return gameId;
    }

    public Map<String, Integer> getPlayerPositions() {
        return playerPositions;
    }
}

