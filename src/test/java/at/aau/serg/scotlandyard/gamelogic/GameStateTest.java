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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class GameStateTest {
    private GameState gameState;
    private MrX mrX;
    private Detective detective;
    private Board board;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        mrX = mock(MrX.class);
        detective = mock(Detective.class);
        gameState.addPlayer("MrX", mrX);
        gameState.addPlayer("Detective", detective);
        board = new Board();
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


}
