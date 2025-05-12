package at.aau.serg.scotlandyard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LobbyUpdateTest {
    private LobbyUpdate lobbyUpdate = new LobbyUpdate();

    @BeforeEach
    void setUp() {
        String message = "message123";
        String gameId = "game123";
        lobbyUpdate = new LobbyUpdate(gameId, message);
    }

    // --- Game ID ---
    @Test
    void testGetGameId() {
        assertEquals("game123", lobbyUpdate.getGameId());
    }

    @Test
    void testSetGameId() {
        lobbyUpdate.setGameId("newGame");
        assertEquals("newGame", lobbyUpdate.getGameId());
    }

    // --- Message ---
    @Test
    void testGetMessage() {
        assertEquals("message123", lobbyUpdate.getMessage());
    }

    @Test
    void testSetMessage() {
        lobbyUpdate.setMessage("newMessage");
        assertEquals("newMessage", lobbyUpdate.getMessage());
    }

}
