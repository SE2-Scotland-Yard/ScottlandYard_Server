package at.aau.serg.scotlandyard.controller;

import at.aau.serg.scotlandyard.dto.JoinResponse;
import at.aau.serg.scotlandyard.dto.LobbyState;
import at.aau.serg.scotlandyard.gamelogic.Lobby;
import at.aau.serg.scotlandyard.gamelogic.LobbyManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LobbyControllerTest {

    @Mock
    private LobbyManager lobbyManager;

    @Mock
    private SimpMessagingTemplate messaging;

    @Mock
    private Lobby lobby;

    @InjectMocks
    private LobbyController lobbyController;

    private final String gameId = UUID.randomUUID().toString();
    private final String playerName = "testPlayer";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLobby_ShouldCreateLobbyAndReturnState() {
        when(lobbyManager.createLobby(true)).thenReturn(lobby);
        when(lobby.getGameId()).thenReturn(gameId);

        LobbyState result = lobbyController.createLobby(true, playerName);

        verify(lobbyManager).createLobby(true);
        verify(lobby).addPlayer(playerName);
        verify(messaging).convertAndSend("/topic/lobby/" + gameId, result);
        assertNotNull(result);
    }

    @Test
    void joinLobby_WhenLobbyNotFound_ShouldReturnNotFound() {
        when(lobbyManager.getLobby(gameId)).thenReturn(null);

        ResponseEntity<JoinResponse> response = lobbyController.joinLobby(gameId, playerName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Lobby mit der ID " + gameId + " wurde nicht gefunden.",
                response.getBody().getMessage());
    }

    @Test
    void joinLobby_WhenLobbyFull_ShouldReturnBadRequest() {
        when(lobbyManager.getLobby(gameId)).thenReturn(lobby);
        when(lobby.addPlayer(playerName)).thenReturn(false);

        ResponseEntity<JoinResponse> response = lobbyController.joinLobby(gameId, playerName);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Lobby ist voll oder bereits gestartet.",
                response.getBody().getMessage());
    }

    @Test
    void joinLobby_WhenSuccess_ShouldReturnOkAndBroadcast() {
        when(lobbyManager.getLobby(gameId)).thenReturn(lobby);
        when(lobby.addPlayer(playerName)).thenReturn(true);

         ResponseEntity<JoinResponse> response = lobbyController.joinLobby(gameId, playerName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playerName + " ist der Lobby " + gameId + " beigetreten.",
                response.getBody().getMessage());

        verify(messaging).convertAndSend(eq("/topic/lobby/" + gameId), any(LobbyState.class));
    }

    @Test
    void leaveLobby_WhenLobbyNotFound_ShouldReturnNotFound() {
        when(lobbyManager.getLobby(gameId)).thenReturn(null);

        ResponseEntity<String> response = lobbyController.leaveLobby(gameId, playerName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Lobby nicht gefunden", response.getBody());
    }

    @Test
    void leaveLobby_WhenSuccess_ShouldRemovePlayerAndBroadcast() {

        when(lobbyManager.getLobby(gameId)).thenReturn(lobby);
        when(lobby.getPlayers()).thenReturn(Set.of("otherPlayer"));

        ResponseEntity<String> response = lobbyController.leaveLobby(gameId, playerName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playerName + " hat die Lobby verlassen", response.getBody());

        verify(lobby).removePlayer(playerName);
        verify(messaging).convertAndSend(eq("/topic/lobby/" + gameId), any(LobbyState.class));
    }

    @Test
    void leaveLobby_WhenEmptyAfterLeave_ShouldRemoveLobby() {
        when(lobbyManager.getLobby(gameId)).thenReturn(lobby);
        when(lobby.getPlayers()).thenReturn(Set.of());

        ResponseEntity<String> response = lobbyController.leaveLobby(gameId, playerName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lobbyManager).removeLobby(gameId);
        verify(messaging, never()).convertAndSend(any(String.class), any(Object.class));
    }

    @Test
    void getPublicLobbies_ShouldReturnListOfLobbyStates() {
        List<Lobby> publicLobbies = List.of(lobby);
        when(lobbyManager.getPublicLobbies()).thenReturn(publicLobbies);

        List<LobbyState> result = lobbyController.getPublicLobbies();

        assertEquals(1, result.size());
        verify(lobbyManager).getPublicLobbies();
    }

    @Test
    void getLobbyStatus_WhenLobbyNotFound_ShouldReturnNotFound() {
        when(lobbyManager.getLobby(gameId)).thenReturn(null);

        ResponseEntity<LobbyState> response = lobbyController.getLobbyStatus(gameId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getLobbyStatus_WhenLobbyExists_ShouldReturnState() {
        when(lobbyManager.getLobby(gameId)).thenReturn(lobby);

        ResponseEntity<LobbyState> response = lobbyController.getLobbyStatus(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}