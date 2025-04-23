package at.aau.serg.scotlandyard.dto;

import java.util.Map;

public class GameOverviewDTO {
    private String gameId;
    private Map<String, String> players; // Spielername → Rolle

    public GameOverviewDTO(String gameId, Map<String, String> players) {
        this.gameId = gameId;
        this.players = players;
    }

    public String getGameId() {
        return gameId;
    }

    public Map<String, String> getPlayers() {
        return players;
    }
}
