package at.aau.serg.scotlandyard.dto;

import at.aau.serg.scotlandyard.gamelogic.Lobby;
import at.aau.serg.scotlandyard.gamelogic.LobbyManager;
import at.aau.serg.scotlandyard.gamelogic.Role;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class LobbyMapperTest {

    @Test
    void testUtilityClassCannotBeInstantiated() {
        assertThrows(UnsupportedOperationException.class, LobbyMapper::new);
    }

    @Test
    void testToLobbyStateMapsAllFields() {
        // Dummy-Lobby vorbereiten
        Lobby lobby = new Lobby("game123", true);
        lobby.addPlayer("Anna");
        lobby.addPlayer("Bob");
        lobby.markReady("Anna");
        lobby.selectRole("Anna", Role.MRX);
        lobby.selectRole("Bob", Role.DETECTIVE);
        lobby.markStarted();

        LobbyState state = LobbyMapper.toLobbyState(lobby);

        assertEquals("game123", state.getGameId());
        assertTrue(state.isPublic());
        assertTrue(state.isStarted());
        assertTrue(state.getPlayers().containsAll(List.of("Anna", "Bob")));
        assertEquals(2, state.getPlayers().size());
        assertEquals(Map.of("Anna", true, "Bob", false), state.getReadyStatus());
        assertEquals(Map.of("Anna", "MRX", "Bob", "DETECTIVE"), state.getSelectedRoles());
        assertEquals(6, state.getMaxPlayers());
    }

    @Test
    void testToLobbyStateWithEmptyLobby() {
        Lobby lobby = new Lobby("empty", false);
        LobbyState state = LobbyMapper.toLobbyState(lobby);

        assertEquals("empty", state.getGameId());
        assertFalse(state.isPublic());
        assertFalse(state.isStarted());
        assertTrue(state.getPlayers().isEmpty());
        assertTrue(state.getReadyStatus().isEmpty());
        assertTrue(state.getSelectedRoles().isEmpty());
        assertEquals(6, state.getMaxPlayers());
    }

    @Test
    void testToLobbyStateWithStartedLobby() {
        Lobby lobby = new Lobby("started", true);
        lobby.addPlayer("Clara");
        lobby.markStarted();

        LobbyState state = LobbyMapper.toLobbyState(lobby);

        assertTrue(state.isStarted());
        assertEquals("started", state.getGameId());
    }

}
