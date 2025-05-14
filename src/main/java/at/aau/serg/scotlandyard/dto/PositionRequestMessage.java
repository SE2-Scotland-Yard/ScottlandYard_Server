package at.aau.serg.scotlandyard.dto;

public class PositionRequestMessage {
    private String gameId;
    private String playerId;

    public PositionRequestMessage() {}

    public PositionRequestMessage(String gameId, String playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public String getGameId() {
        return gameId;
    }

    public String getPlayerId() {
        return playerId;
    }
}

