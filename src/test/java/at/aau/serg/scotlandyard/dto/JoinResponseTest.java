package at.aau.serg.scotlandyard.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class JoinResponseTest {

    @Test
    void testConstructorAndGetter() {
        JoinResponse response = new JoinResponse("Erfolg!");
        assertEquals("Erfolg!", response.getMessage());
    }

    @Test
    void testSetter() {
        JoinResponse response = new JoinResponse("Alt");
        response.setMessage("Neu");
        assertEquals("Neu", response.getMessage());
    }

    @Test
    void testNullMessage() {
        JoinResponse response = new JoinResponse(null);
        assertNull(response.getMessage());
        response.setMessage("Hallo");
        assertEquals("Hallo", response.getMessage());
    }

}
