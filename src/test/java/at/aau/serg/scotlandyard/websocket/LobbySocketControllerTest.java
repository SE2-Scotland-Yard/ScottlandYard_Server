package at.aau.serg.scotlandyard.websocket;

import at.aau.serg.scotlandyard.dto.*;
import at.aau.serg.scotlandyard.gamelogic.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.messaging.simp.SimpMessagingTemplate;



import static org.mockito.Mockito.*;

class LobbySocketControllerTest {

    private LobbyManager lobbyManager;
    private GameManager gameManager;
    private SimpMessagingTemplate messaging;
    private LobbySocketController controller;
    private Lobby lobby;
    private GameState gameState;

    @BeforeEach
    void setUp() {
        lobbyManager = mock(LobbyManager.class);
        gameManager = mock(GameManager.class);
        messaging = mock(SimpMessagingTemplate.class);
        controller = new LobbySocketController(lobbyManager, gameManager, messaging);

        lobby = mock(Lobby.class);
        gameState = mock(GameState.class);
    }

    @Test
    void testHandleReady_LobbyNotFound() {
        when(lobbyManager.getLobby("game1")).thenReturn(null);

        ReadyMessage msg = new ReadyMessage();
        msg.setGameId("game1");
        msg.setPlayerId("Anna");

        controller.handleReady(msg);

        verifyNoInteractions(messaging);
    }

    @Test
    void testHandleReady_NormalFlow() {
        when(lobbyManager.getLobby("game2")).thenReturn(lobby);
        when(lobby.allReady()).thenReturn(false);
        when(lobby.hasEnoughPlayers()).thenReturn(true);
        when(lobby.isStarted()).thenReturn(false);
        when(lobby.getGameId()).thenReturn("game2");

        ReadyMessage msg = new ReadyMessage();
        msg.setGameId("game2");
        msg.setPlayerId("Bob");

        try (MockedStatic<LobbyMapper> mocked = mockStatic(LobbyMapper.class)) {
            LobbyState mockLobbyState = mock(LobbyState.class);
            mocked.when(() -> LobbyMapper.toLobbyState(any(Lobby.class))).thenReturn(mockLobbyState);

            controller.handleReady(msg);

            verify(lobby).markReady("Bob");
            verify(messaging).convertAndSend("/topic/lobby/game2", mockLobbyState);
        }
    }

    /*@Test
    void testHandleReady_AllReadyAndStart() {
        when(lobbyManager.getLobby("game3")).thenReturn(lobby);
        when(lobby.allReady()).thenReturn(true);
        when(lobby.hasEnoughPlayers()).thenReturn(true);
        when(lobby.isStarted()).thenReturn(false);
        when(lobby.getGameId()).thenReturn("game3");

        ReadyMessage msg = new ReadyMessage();
        msg.setGameId("game3");
        msg.setPlayerId("Clara");

        try (MockedStatic<LobbyMapper> mocked = mockStatic(LobbyMapper.class)) {
            LobbyState mockLobbyState = mock(LobbyState.class);
            mocked.when(() -> LobbyMapper.toLobbyState(any(Lobby.class))).thenReturn(mockLobbyState);

            when(gameManager.getOrCreateGame("game3")).thenReturn(gameState);
            when(lobby.getPlayers()).thenReturn(Set.of("Clara"));
            when(lobby.getSelectedRole("Clara")).thenReturn(Role.MRX);

            MrX mrX = mock(MrX.class);
            when(mrX.getName()).thenReturn("Clara");
            when(gameState.getAllPlayers()).thenReturn(Map.of("Clara", mrX));
            when(gameState.getCurrentPlayerName()).thenReturn("Clara");

            try (MockedStatic<GameMapper> gameMapperMocked = mockStatic(GameMapper.class)) {
                GameUpdate gameUpdate = new GameUpdate("game3", Map.of("Clara", 42), "Clara");
                gameMapperMocked.when(() -> GameMapper.mapToGameUpdate(eq("game3"), anyMap(), eq("Clara")))
                        .thenReturn(gameUpdate);

                controller.handleReady(msg);

                verify(lobby).markReady("Clara");
                verify(lobby).markStarted();
                verify(messaging, times(2)).convertAndSend(eq("/topic/lobby/game3"), any(LobbyState.class));
                verify(messaging).convertAndSend(eq("/topic/game/game3"), eq(gameUpdate));
            }
        }
    }*/

    @Test
    void testSelectRole_LobbyNotFound() {
        when(lobbyManager.getLobby("gX")).thenReturn(null);
        RoleSelectionMessage msg = new RoleSelectionMessage();
        msg.setGameId("gX");
        msg.setPlayerId("Anna");
        msg.setRole(Role.MRX);

        controller.selectRole(msg);

        verifyNoInteractions(messaging);
    }

    @Test
    void testSelectRole_NormalFlow() {
        when(lobbyManager.getLobby("game4")).thenReturn(lobby);
        when(lobby.getGameId()).thenReturn("game4");

        RoleSelectionMessage msg = new RoleSelectionMessage();
        msg.setGameId("game4");
        msg.setPlayerId("Bob");
        msg.setRole(Role.DETECTIVE);

        try (MockedStatic<LobbyMapper> mocked = mockStatic(LobbyMapper.class)) {
            LobbyState mockLobbyState = mock(LobbyState.class);
            mocked.when(() -> LobbyMapper.toLobbyState(any(Lobby.class))).thenReturn(mockLobbyState);

            controller.selectRole(msg);

            verify(lobby).selectRole("Bob", Role.DETECTIVE);
            verify(messaging).convertAndSend("/topic/lobby/game4", mockLobbyState);
        }
    }

    /*
    @Test
    void testGameUpdate() {
        GameState game = mock(GameState.class);
        Map<String, Player> playerMap = Map.of("Anna", mock(Player.class));
        when(game.getAllPlayers()).thenReturn(playerMap);

        GameUpdate gameUpdate = new GameUpdate("game5", Map.of("Anna", 42), "Anna");

        try (MockedStatic<GameMapper> mocked = mockStatic(GameMapper.class)) {
            mocked.when(() -> GameMapper.mapToGameUpdate(eq("game5"), eq(playerMap), "Anna"))
                    .thenReturn(gameUpdate);

            controller.gameUpdate("game5", game);

            verify(messaging).convertAndSend(eq("/topic/game/updategame5"), eq(gameUpdate));
        }
    }*/
}
