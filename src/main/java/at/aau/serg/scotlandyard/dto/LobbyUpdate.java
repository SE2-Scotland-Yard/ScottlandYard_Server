package at.aau.serg.scotlandyard.dto;

public class LobbyUpdate {
    private String gameId;
    private String message;

    public LobbyUpdate() {
    }

    public LobbyUpdate(String gameId, String message) {
        this.gameId = gameId;
        this.message = message;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
