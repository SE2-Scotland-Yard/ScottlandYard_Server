package at.aau.serg.scotlandyard.controller;

import at.aau.serg.scotlandyard.dto.GameOverviewDTO;
import at.aau.serg.scotlandyard.gamelogic.GameManager;
import at.aau.serg.scotlandyard.gamelogic.GameState;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Mock
    private GameManager gameManager;

    @Mock
    private GameState gameState;

    @Mock
    private Player player;

    @InjectMocks
    private GameController gameController;

    private final String GAME_ID = "testGame";
    private final String PLAYER_NAME = "testPlayer";
    private final int POSITION = 42;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMoves_WhenGameNotFound_ReturnsNotFound() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        ResponseEntity<?> response = gameController.getMoves(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Game mit ID '" + GAME_ID + "' nicht gefunden.", response.getBody());
    }

    @Test
    void getMoves_WhenGameExists_ReturnsAllowedMoves() {
        Map<Integer, Ticket> moves = new HashMap<>();
        moves.put(1, Ticket.TAXI);
        moves.put(2, Ticket.BUS);
        moves.put(3, Ticket.UNDERGROUND);
        List<Entry<Integer, Ticket>> expectedMoves = new ArrayList<>(moves.entrySet());

        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getAllowedMoves(PLAYER_NAME)).thenReturn(expectedMoves);

        ResponseEntity<?> response = gameController.getMoves(GAME_ID, PLAYER_NAME);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);

        @SuppressWarnings("unchecked")
        List<Entry<Integer, String>> responseBody = (List<Entry<Integer, String>>) response.getBody();
        assertEquals(3, responseBody.size());
        assertEquals("TAXI", responseBody.get(0).getValue());
        assertEquals("BUS", responseBody.get(1).getValue());
        assertEquals("UNDERGROUND", responseBody.get(2).getValue());
    }

    @Test
    void move_WhenInvalidTicket_ReturnsErrorMessage() {
        Map<String, String> result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, "INVALID_TICKET");

        assertEquals("Ungültiges Ticket: INVALID_TICKET", result.get("message"));
    }

    @Test
    void move_WhenGameNotFound_ReturnsGameNotFound() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        Map<String, String> result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, "TAXI");

        assertEquals("Spiel nicht gefunden!", result.get("message"));
    }

    @Test
    void move_WhenPlayerNotFound_ReturnsErrorMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getAllPlayers()).thenReturn(new HashMap<>());

        Map<String, String> result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, "TAXI");

        assertEquals("Spieler " + PLAYER_NAME + " existiert nicht!", result.get("message"));
    }

    @Test
    void move_WhenInvalidMove_ReturnsErrorMessage() {
        Map<String, Player> players = new HashMap<>();
        players.put(PLAYER_NAME, player);

        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getAllPlayers()).thenReturn(players);
        when(gameState.movePlayer(PLAYER_NAME, POSITION, Ticket.TAXI)).thenReturn(false);

        Map<String, String> result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, "TAXI");

        assertEquals("Ungültiger Zug!", result.get("message"));
    }

    @Test
    void move_WhenValidMove_ReturnsSuccessMessage() {
        Map<String, Player> players = new HashMap<>();
        players.put(PLAYER_NAME, player);

        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getAllPlayers()).thenReturn(players);
        when(gameState.movePlayer(PLAYER_NAME, POSITION, Ticket.TAXI)).thenReturn(true);
        when(gameState.getWinner()).thenReturn(GameState.Winner.NONE);

        Map<String, String> result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, "TAXI");

        assertEquals("Spieler " + PLAYER_NAME + " bewegt sich zu " + POSITION + " in Spiel " + GAME_ID, result.get("message"));
    }

    @Test
    void move_WhenMoveWinsGame_ReturnsWinnerMessage() {
        Map<String, Player> players = new HashMap<>();
        players.put(PLAYER_NAME, player);

        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getAllPlayers()).thenReturn(players);
        when(gameState.movePlayer(PLAYER_NAME, POSITION, Ticket.TAXI)).thenReturn(true);
        when(gameState.getWinner()).thenReturn(GameState.Winner.MR_X);

        Map<String, String> result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, "TAXI");

        assertEquals("Mr.X hat gewonnen!", result.get("message"));
    }

    @Test
    void moveDouble_WhenGameNotFound_ReturnsGameNotFound() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        String result = gameController.moveDouble(GAME_ID, PLAYER_NAME, POSITION, Ticket.TAXI, POSITION + 1, Ticket.BUS);

        assertEquals(GameController.GAME_NOT_FOUND, result);
    }

    @Test
    void moveDouble_WhenInvalidMove_ReturnsErrorMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.moveMrXDouble(PLAYER_NAME, POSITION, Ticket.TAXI, POSITION + 1, Ticket.BUS)).thenReturn(false);

        String result = gameController.moveDouble(GAME_ID, PLAYER_NAME, POSITION, Ticket.TAXI, POSITION + 1, Ticket.BUS);

        assertEquals("Ungültiger Doppelzug!", result);
    }

    @Test
    void moveDouble_WhenValidMove_ReturnsSuccessMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.moveMrXDouble(PLAYER_NAME, POSITION, Ticket.TAXI, POSITION + 1, Ticket.BUS)).thenReturn(true);

        String result = gameController.moveDouble(GAME_ID, PLAYER_NAME, POSITION, Ticket.TAXI, POSITION + 1, Ticket.BUS);

        assertEquals("MrX machte einen Doppelzug: " + POSITION + " → " + (POSITION + 1), result);
    }

    @Test
    void getMrXPosition_WhenGameNotFound_ReturnsGameNotFound() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        String result = gameController.getMrXPosition(GAME_ID);

        assertEquals(GameController.GAME_NOT_FOUND, result);
    }

    @Test
    void getMrXPosition_WhenGameExists_ReturnsPosition() {
        String expectedPosition = "123";
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getVisibleMrXPosition()).thenReturn(expectedPosition);

        String result = gameController.getMrXPosition(GAME_ID);

        assertEquals(expectedPosition, result);
    }

    @Test
    void getMrXHistory_WhenGameNotFound_ReturnsGameNotFound() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        List<String> result = gameController.getMrXHistory(GAME_ID);

        assertEquals(List.of(GameController.GAME_NOT_FOUND), result);
    }

    @Test
    void getMrXHistory_WhenGameExists_ReturnsHistory() {
        List<String> expectedHistory = List.of("Move 1", "Move 2");
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getMrXMoveHistory()).thenReturn(expectedHistory);

        List<String> result = gameController.getMrXHistory(GAME_ID);

        assertEquals(expectedHistory, result);
    }

    @Test
    void getAllGames_ReturnsListOfGameOverviewDTOs() {
        Set<String> gameIds = new HashSet<>();
        gameIds.add("game1");
        gameIds.add("game2");
        when(gameManager.getAllGameIds()).thenReturn(gameIds);

        GameState gameState1 = mock(GameState.class);
        GameState gameState2 = mock(GameState.class);

        when(gameManager.getGame("game1")).thenReturn(gameState1);
        when(gameManager.getGame("game2")).thenReturn(gameState2);
        when(gameState1.getAllPlayers()).thenReturn(new HashMap<>());
        when(gameState2.getAllPlayers()).thenReturn(new HashMap<>());

        List<GameOverviewDTO> result = gameController.getAllGames();

        assertEquals(2, result.size());

        List<String> resultIds = new ArrayList<>();
        resultIds.add(result.get(0).getGameId());
        resultIds.add(result.get(1).getGameId());
        Collections.sort(resultIds);
        assertEquals(Arrays.asList("game1", "game2"), resultIds);
    }

    @Test
    void getWinner_WhenGameNotFound_ReturnsGameNotFound() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        String result = gameController.getWinner(GAME_ID);

        assertEquals(GameController.GAME_NOT_FOUND, result);
    }

    @Test
    void getWinner_WhenMrXWins_ReturnsMrXWonMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getWinner()).thenReturn(GameState.Winner.MR_X);

        String result = gameController.getWinner(GAME_ID);

        assertEquals("Mr.X hat gewonnen!", result);
    }

    @Test
    void getWinner_WhenDetectivesWin_ReturnsDetectivesWonMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getWinner()).thenReturn(GameState.Winner.DETECTIVE);

        String result = gameController.getWinner(GAME_ID);

        assertEquals("Detektive haben gewonnen!", result);
    }

    @Test
    void getWinner_WhenGameStillRunning_ReturnsGameStillRunningMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getWinner()).thenReturn(GameState.Winner.NONE);

        String result = gameController.getWinner(GAME_ID);

        assertEquals("Spiel läuft noch.", result);
    }
}