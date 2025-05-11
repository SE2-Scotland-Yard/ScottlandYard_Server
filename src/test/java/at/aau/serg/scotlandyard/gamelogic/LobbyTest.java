package at.aau.serg.scotlandyard.gamelogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class LobbyTest {

    private Lobby lobby;

    @BeforeEach
    void setUp() {
        lobby = new Lobby("lobby", true);
    }

    @Test
    void testAddPlayer() {
        boolean success = lobby.addPlayer("Player");
        assertTrue(success);
        assertEquals(1, lobby.getPlayers().size());
    }

    @Test
    void testAddPlayerFull() {
        for(int i = 1; i <= 6; i++ ){
            lobby.addPlayer("Player" + i);
        }

        boolean success = lobby.addPlayer("Player7");
        assertFalse(success);
        assertEquals(6, lobby.getPlayers().size());
    }

    @Test
    void testRemovePlayer() {
        lobby.addPlayer("Player");
        boolean success = lobby.removePlayer("Player");
        assertTrue(success);
        assertEquals(0, lobby.getPlayers().size());
    }
}
