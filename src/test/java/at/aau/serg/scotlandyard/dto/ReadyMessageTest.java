package at.aau.serg.scotlandyard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReadyMessageTest {
    private ReadyMessage readyMessage = new ReadyMessage();

    @BeforeEach
    void setUp() {
        String playerId = "playerId";
        String gameId = "game123";
        readyMessage = new ReadyMessage(gameId, playerId);
    }

    // --- Game ID ---
    @Test
    void testGetGameId() {
        assertEquals("game123", readyMessage.getGameId());
    }

    @Test
    void testSetGameId() {
        readyMessage.setGameId("newGame");
        assertEquals("newGame", readyMessage.getGameId());
    }

    // --- Message ---
    @Test
    void testGetMessage() {
        assertEquals("playerId", readyMessage.getPlayerId());
    }

    @Test
    void testSetMessage() {
        readyMessage.setPlayerId("newPlayerId");
        assertEquals("newPlayerId", readyMessage.getPlayerId());
    }

}
