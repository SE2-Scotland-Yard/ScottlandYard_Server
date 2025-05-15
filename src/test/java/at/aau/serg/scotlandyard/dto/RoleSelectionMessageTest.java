package at.aau.serg.scotlandyard.dto;

import at.aau.serg.scotlandyard.gamelogic.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleSelectionMessageTest {

    @Test
    void testSetAndGetGameId() {
        RoleSelectionMessage msg = new RoleSelectionMessage();
        msg.setGameId("game42");
        assertEquals("game42", msg.getGameId());
    }

    @Test
    void testSetAndGetPlayerId() {
        RoleSelectionMessage msg = new RoleSelectionMessage();
        msg.setPlayerId("playerX");
        assertEquals("playerX", msg.getPlayerId());
    }

    @Test
    void testSetAndGetRole() {
        RoleSelectionMessage msg = new RoleSelectionMessage();
        msg.setRole(Role.MRX);
        assertEquals(Role.MRX, msg.getRole());
    }

    @Test
    void testNullValues() {
        RoleSelectionMessage msg = new RoleSelectionMessage();
        msg.setGameId(null);
        msg.setPlayerId(null);
        msg.setRole(null);
        assertNull(msg.getGameId());
        assertNull(msg.getPlayerId());
        assertNull(msg.getRole());
    }

}
