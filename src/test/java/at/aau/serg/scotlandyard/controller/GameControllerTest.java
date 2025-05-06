package at.aau.serg.scotlandyard.controller;

import at.aau.serg.scotlandyard.dto.GameOverviewDTO;
import at.aau.serg.scotlandyard.gamelogic.GameManager;
import at.aau.serg.scotlandyard.gamelogic.GameState;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameControllerTest {

    @Mock
    private GameManager gameManager;

    @Mock
    private GameState gameState;

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
    void getMoves_WhenGameNotFound_ReturnsEmptyList() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        List<Integer> result = gameController.getMoves(GAME_ID, PLAYER_NAME);

        assertTrue(result.isEmpty());
    }

    @Test
    void getMoves_WhenGameExists_ReturnsAllowedMoves() {
        List<Integer> expectedMoves = List.of(1, 2, 3);
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.getAllowedMoves(PLAYER_NAME)).thenReturn(expectedMoves);

        List<Integer> result = gameController.getMoves(GAME_ID, PLAYER_NAME);

        assertEquals(expectedMoves, result);
    }

    @Test
    void move_WhenGameNotFound_ReturnsGameNotFound() {
        when(gameManager.getGame(GAME_ID)).thenReturn(null);

        String result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, Ticket.TAXI);

        assertEquals(GameController.GAME_NOT_FOUND, result);
    }

    @Test
    void move_WhenInvalidMove_ReturnsErrorMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.movePlayer(PLAYER_NAME, POSITION, Ticket.TAXI)).thenReturn(false);

        String result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, Ticket.TAXI);

        assertEquals("Ungültiger Zug!", result);
    }

    @Test
    void move_WhenValidMove_ReturnsSuccessMessage() {
        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.movePlayer(PLAYER_NAME, POSITION, Ticket.TAXI)).thenReturn(true);
        when(gameState.getWinner()).thenReturn(GameState.Winner.NONE);

        String result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, Ticket.TAXI);

        assertEquals("Spieler " + PLAYER_NAME + " bewegt sich zu " + POSITION + " in Spiel " + GAME_ID, result);
    }

    @Test
    void move_WhenMoveWinsGame_ReturnsWinnerMessage() {

        when(gameManager.getGame(GAME_ID)).thenReturn(gameState);
        when(gameState.movePlayer(PLAYER_NAME, POSITION, Ticket.TAXI)).thenReturn(true);
        when(gameState.getWinner()).thenReturn(GameState.Winner.MR_X);

        String result = gameController.move(GAME_ID, PLAYER_NAME, POSITION, Ticket.TAXI);

        assertEquals("Mr.X hat gewonnen!", result); }

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

        Set<String> gameIds = Set.of("game1", "game2");
        when(gameManager.getAllGameIds()).thenReturn(gameIds);

        GameState gameState1 = mock(GameState.class);
        GameState gameState2 = mock(GameState.class);

        when(gameManager.getGame("game1")).thenReturn(gameState1);
        when(gameManager.getGame("game2")).thenReturn(gameState2);
        when(gameState1.getAllPlayers()).thenReturn(new HashMap<>());
        when(gameState2.getAllPlayers()).thenReturn(new HashMap<>());

        List<GameOverviewDTO> result = gameController.getAllGames();

        assertEquals(2, result.size());

        List<String> resultIds = result.stream()
                .map(GameOverviewDTO::getGameId)
                .sorted()
                .toList();
        assertEquals(List.of("game1", "game2"), resultIds);
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