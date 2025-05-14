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

    private final String GAME_ID = UUID.randomUUID().toString();
    private final String PLAYER_NAME = "testPlayer";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLobby_ShouldCreateLobbyAndReturnState() {
        when(lobbyManager.createLobby(true)).thenReturn(lobby);
        when(lobby.getGameId()).thenReturn(GAME_ID);

        LobbyState result = lobbyController.createLobby(true, PLAYER_NAME);

        verify(lobbyManager).createLobby(true);
        verify(lobby).addPlayer(PLAYER_NAME);
        verify(messaging).convertAndSend("/topic/lobby/" + GAME_ID, result);
        assertNotNull(result);
    }

    @Test
    void joinLobby_WhenLobbyNotFound_ShouldReturnNotFound() {
        when(lobbyManager.getLobby(GAME_ID)).thenReturn(null);

        ResponseEntity<JoinResponse> response = lobbyController.joinLobby(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Lobby mit der ID " + GAME_ID + " wurde nicht gefunden.",
                response.getBody().getMessage());
    }

    @Test
    void joinLobby_WhenLobbyFull_ShouldReturnBadRequest() {
        when(lobbyManager.getLobby(GAME_ID)).thenReturn(lobby);
        when(lobby.addPlayer(PLAYER_NAME)).thenReturn(false);

        ResponseEntity<JoinResponse> response = lobbyController.joinLobby(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Lobby ist voll oder bereits gestartet.",
                response.getBody().getMessage());
    }

    @Test
    void joinLobby_WhenSuccess_ShouldReturnOkAndBroadcast() {
        when(lobbyManager.getLobby(GAME_ID)).thenReturn(lobby);
        when(lobby.addPlayer(PLAYER_NAME)).thenReturn(true);

         ResponseEntity<JoinResponse> response = lobbyController.joinLobby(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PLAYER_NAME + " ist der Lobby " + GAME_ID + " beigetreten.",
                response.getBody().getMessage());

        verify(messaging).convertAndSend(eq("/topic/lobby/" + GAME_ID), any(LobbyState.class));
    }

    @Test
    void leaveLobby_WhenLobbyNotFound_ShouldReturnNotFound() {
        when(lobbyManager.getLobby(GAME_ID)).thenReturn(null);

        ResponseEntity<String> response = lobbyController.leaveLobby(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Lobby nicht gefunden", response.getBody());
    }

    @Test
    void leaveLobby_WhenSuccess_ShouldRemovePlayerAndBroadcast() {

        when(lobbyManager.getLobby(GAME_ID)).thenReturn(lobby);
        when(lobby.getPlayers()).thenReturn(Set.of("otherPlayer"));

        ResponseEntity<String> response = lobbyController.leaveLobby(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PLAYER_NAME + " hat die Lobby verlassen", response.getBody());

        verify(lobby).removePlayer(PLAYER_NAME);
        verify(messaging).convertAndSend(eq("/topic/lobby/" + GAME_ID), any(LobbyState.class));
    }

    @Test
    void leaveLobby_WhenEmptyAfterLeave_ShouldRemoveLobby() {
        when(lobbyManager.getLobby(GAME_ID)).thenReturn(lobby);
        when(lobby.getPlayers()).thenReturn(Set.of());

        ResponseEntity<String> response = lobbyController.leaveLobby(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(lobbyManager).removeLobby(GAME_ID);
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
        when(lobbyManager.getLobby(GAME_ID)).thenReturn(null);

        ResponseEntity<LobbyState> response = lobbyController.getLobbyStatus(GAME_ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getLobbyStatus_WhenLobbyExists_ShouldReturnState() {
        when(lobbyManager.getLobby(GAME_ID)).thenReturn(lobby);

        ResponseEntity<LobbyState> response = lobbyController.getLobbyStatus(GAME_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}