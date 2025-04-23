package at.aau.serg.scotlandyard.dto;

import at.aau.serg.scotlandyard.gamelogic.Role;

public class RoleSelectionMessage {
    private String gameId;
    private String playerId;
    private Role role;

    // Getter + Setter
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
