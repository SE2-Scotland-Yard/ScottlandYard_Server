package at.aau.serg.scotlandyard.gamelogic;
import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.PlayerTickets;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


public class GameStateTest {
    private GameState gameState;
    private MrX mrX;
    private Detective detective;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        mrX = mock(MrX.class);
        detective = mock(Detective.class);
        gameState.addPlayer("MrX", mrX);
        gameState.addPlayer("Detective", detective);
    }

    @Test
    void testAddPlayer() {
        assertEquals(2, gameState.getAllPlayers().size());
        assertTrue(gameState.getAllPlayers().containsKey("MrX"));
        assertTrue(gameState.getAllPlayers().containsKey("Detective"));
    }

    @Test
    void testGetAllowedMoves(){
        when(mrX.getPosition()).thenReturn(1);
        when(mrX.getTickets()).thenReturn(getDefaultTickets());

        List<Integer> allowedMoves = gameState.getAllowedMoves("MrX");
        assertEquals(5, allowedMoves.size());
    }

    private static PlayerTickets getDefaultTickets() {
        Map<Ticket, Integer> initialTickets = new EnumMap<>(Ticket.class);

        initialTickets.put(Ticket.TAXI, 4);
        initialTickets.put(Ticket.BUS, 3);
        initialTickets.put(Ticket.UNDERGROUND, 3);
        initialTickets.put(Ticket.BLACK, 5);
        initialTickets.put(Ticket.DOUBLE, 2);

        return new PlayerTickets(initialTickets);
    }

    @Test
    void testGetAllowedMovesPlayerIsNull(){
        List<Integer> allowedMoves = gameState.getAllowedMoves("Nothing");
        assertTrue(allowedMoves.isEmpty());
    }

    @Test
    void testMovePlayerMrX(){
        when(mrX.isValidMove(anyInt(), any(Ticket.class), any(Board.class))).thenReturn(true);
        boolean successful = gameState.movePlayer("MrX", 1, Ticket.TAXI);

        verify(mrX).move(eq(1), eq(Ticket.TAXI), any(Board.class));
        assertTrue(successful);

    }

    @Test
    void testMovePlayerMrXInvalid(){
        when(mrX.isValidMove(anyInt(), any(Ticket.class), any(Board.class))).thenReturn(false);
        boolean successful = gameState.movePlayer("MrX", 1, Ticket.TAXI);

        assertFalse(successful);
    }

    @Test
    void testMovePlayerDetective(){
        when(detective.isValidMove(anyInt(), any(Ticket.class), any(Board.class))).thenReturn(true);
        boolean successful = gameState.movePlayer("Detective", 1, Ticket.TAXI);

        verify(detective).move(eq(1), eq(Ticket.TAXI), any(Board.class));
        assertTrue(successful);
    }

    @Test
    void testMovePlayerDetectivePositionTaken(){
        Detective detective2 =  mock(Detective.class);
        gameState.addPlayer("Detective2", detective2);
        when(detective2.isValidMove(anyInt(), any(Ticket.class), any(Board.class))).thenReturn(false);
        when(detective.getPosition()).thenReturn(1);

        boolean successful = gameState.movePlayer("Detective2", 1, Ticket.TAXI);

        assertFalse(successful);

    }

    @Test
    void testMovePlayerDetectiveInvalid(){
        when(detective.isValidMove(anyInt(), any(Ticket.class), any(Board.class))).thenReturn(false);
        boolean successful = gameState.movePlayer("Detective", 1, Ticket.TAXI);

        assertFalse(successful);
    }

    @Test
    void testMovePlayerNull(){
        boolean successful = gameState.movePlayer("Nothing", 1, Ticket.TAXI);
        assertFalse(successful);
    }

    @Test
    void testMoveMrXDouble(){
        boolean successful = gameState.moveMrXDouble("MrX", 1, Ticket.TAXI, 1, Ticket.TAXI);
        assertTrue(successful);
    }

    @Test
    void testMoveMrXDoubleInvalid(){
        boolean successful = gameState.moveMrXDouble("Detective", 1, Ticket.TAXI, 1, Ticket.TAXI);
        assertFalse(successful);
    }

    @Test
    void testGetVisibleMrXPositionRoundNIsotReveal(){
        String position = gameState.getVisibleMrXPosition();
        assertEquals("?", position);
    }

    @Test
    void testGetVisibleMrXPositionRoundIsReveal(){
        when(mrX.getPosition()).thenReturn(1);
        when(mrX.isValidMove(anyInt(), any(Ticket.class), any(Board.class))).thenReturn(true);

        gameState.movePlayer("MrX", 1, Ticket.TAXI);
        gameState.movePlayer("MrX", 1, Ticket.TAXI);
        gameState.movePlayer("MrX", 1, Ticket.TAXI);


        String position = gameState.getVisibleMrXPosition();
        assertEquals("1", position);
    }

    @Test void testGetVisibleMrXPositionMrXIsNull(){
        GameState gameState = new GameState();
        String position = gameState.getVisibleMrXPosition();
        assertEquals("MrX nicht im Spiel", position);
    }

    @Test
    void testGetMrXMoveHistory(){
        when(mrX.isValidMove(anyInt(), any(Ticket.class), any(Board.class))).thenReturn(true);

        gameState.movePlayer("MrX", 1, Ticket.TAXI);
        gameState.movePlayer("MrX", 1, Ticket.TAXI);
        gameState.movePlayer("MrX", 1, Ticket.TAXI);

        List<String> history = gameState.getMrXMoveHistory();
        assertEquals(3, history.size());
    }

    @Test
    void testGetWinnerNone() throws Exception {
        RoundManager roundManager = mock(RoundManager.class);
        when(roundManager.isGameOver()).thenReturn(false);

        Field nameField = GameState.class.getDeclaredField("roundManager");
        nameField.setAccessible(true); // Make private field accessible
        nameField.set(gameState, roundManager);

        assertEquals(GameState.Winner.NONE, gameState.getWinner());
    }

    @Test
    void testGetWinnerDetective() throws Exception {
        RoundManager roundManager = mock(RoundManager.class);
        when(roundManager.isGameOver()).thenReturn(true);
        when(roundManager.isMrXCaptured()).thenReturn(true);

        Field nameField = GameState.class.getDeclaredField("roundManager");
        nameField.setAccessible(true); // Make private field accessible
        nameField.set(gameState, roundManager);

        assertEquals(GameState.Winner.DETECTIVE, gameState.getWinner());
    }

    @Test
    void testGetWinnerMrX() throws Exception {
        RoundManager roundManager = mock(RoundManager.class);
        when(roundManager.isGameOver()).thenReturn(true);
        when(roundManager.isMrXCaptured()).thenReturn(false);

        Field nameField = GameState.class.getDeclaredField("roundManager");
        nameField.setAccessible(true); // Make private field accessible
        nameField.set(gameState, roundManager);

        assertEquals(GameState.Winner.MR_X, gameState.getWinner());
    }

    @Test
    void testGetRevealRounds(){
        List<Integer> revealRounds = gameState.getRevealRounds();
        assertEquals(List.of(3, 8, 13, 18, 24), revealRounds);
    }

    @Test
    void testGetBoard(){
        Board board = gameState.getBoard();
        assertNotNull(board);
    }

}
