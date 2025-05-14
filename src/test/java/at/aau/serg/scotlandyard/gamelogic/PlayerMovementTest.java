package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.dto.GameMapper;
import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerMovementTest {

    private Board board;
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    void setup() {
        board = new Board();
        messagingTemplate = mock(SimpMessagingTemplate.class);
    }

    @Test
    void testValidMove_ShouldUpdatePositionAndUseTicket() {
        Detective detective = new Detective("Maxmustermann");
        int from = detective.getPosition();
        List<Edge> connections = board.getConnectionsFrom(from);

        Edge validEdge = connections.stream()
                .filter(e -> detective.getTickets().hasTicket(e.getTicket()))
                .findFirst().orElseThrow();

        detective.move(validEdge.getTo(), validEdge.getTicket(), board);

        assertEquals(validEdge.getTo(), detective.getPosition());
        assertEquals(9, detective.getTickets().getTicketCount(validEdge.getTicket()));
    }

    @Test
    void testInvalidMove_ShouldThrowException() {
        Detective detective = new Detective("Maxmustermann");
        assertThrows(IllegalArgumentException.class, () ->
                detective.move(999, Ticket.TAXI, board));
    }

    @Test
    void testMovePlayer_ValidMove_ShouldReturnTrue() {
        GameState game = new GameState("1234", messagingTemplate);
        Detective detective = new Detective("Alice");
        game.addPlayer("Alice", detective);

        int from = detective.getPosition();
        List<Edge> possibleEdges = game.getBoard().getConnectionsFrom(from);

        Edge validEdge = possibleEdges.stream()
                .filter(edge -> detective.getTickets().hasTicket(edge.getTicket()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Keine gültige Bewegung für den Detektiv gefunden."));

        int to = validEdge.getTo();
        Ticket ticket = validEdge.getTicket();

        boolean moved = game.movePlayer("Alice", to, ticket);

        assertTrue(moved, "Ein gültiger Zug sollte erfolgreich sein.");
        assertEquals(to, detective.getPosition(), "Die Position des Detektivs sollte aktualisiert worden sein.");
        assertEquals(9, detective.getTickets().getTicketCount(ticket), "Das Ticket sollte verwendet worden sein.");

    }

    @Test
    void testMovePlayer_InvalidTarget_ShouldReturnFalse() {
        GameState game = new GameState("1234", messagingTemplate);
        Detective det = new Detective("Maxmustermann");
        game.addPlayer("Bob", det);

        boolean moved = game.movePlayer("Bob", 999, Ticket.TAXI);
        assertFalse(moved);
    }

    @Test
    void testVisibleMrXPosition_ShouldReturnPositionOnRevealRound() {
        GameState game = new GameState("1234", messagingTemplate);
        MrX mrX = new MrX("Maxmustermann");
        game.addPlayer("X", mrX);

        moveMrXToRound(game, mrX, 3);

        String visible = game.getVisibleMrXPosition();
        assertEquals(String.valueOf(mrX.getPosition()), visible);
    }

    @Test
    void testInvisibleMrXPosition_ShouldReturnQuestionMark() {
        GameState game = new GameState("1234", messagingTemplate);
        MrX mrX = new MrX("Maxmustermann");
        game.addPlayer("X", mrX);

        moveMrXToRound(game, mrX, 1);

        String visible = game.getVisibleMrXPosition();
        assertEquals("?", visible);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 5, 6, 7})
    void testMrXShouldBeInvisibleInTheseRounds(int roundsToMove) {
        GameState game = new GameState("1234", messagingTemplate);
        MrX mrX = new MrX("Maxmustermann");
        game.addPlayer("X", mrX);

        moveMrXToRound(game, mrX, roundsToMove);

        if (!game.getRevealRounds().contains(roundsToMove)) {
            assertEquals("?", game.getVisibleMrXPosition());
        }
    }

    @Test
    void testMrXDoubleMove_ShouldExecuteTwoMovesAndStoreHistory() {

        GameState game = new GameState("1234", null);
        MrX mrX = new MrX("Maxmustermann");
        game.addPlayer("X", mrX);

        int from = mrX.getPosition();
        Edge first = board.getConnectionsFrom(from).get(0);
        Edge second = board.getConnectionsFrom(first.getTo()).get(0);

        boolean moved = game.moveMrXDouble("X",
                first.getTo(), first.getTicket(),
                second.getTo(), second.getTicket());

        assertTrue(moved, "Double move should be successful");
        assertEquals(second.getTo(), mrX.getPosition(), "Position should be updated to second move");
        assertEquals(2, game.getMrXMoveHistory().size(), "History should contain both moves");
    }

    private void moveMrXToRound(GameState game, MrX mrX, int targetRound) {
        int currentPos = mrX.getPosition();

        for (int i = 1; i <= targetRound; i++) {
            Edge edge = board.getConnectionsFrom(currentPos).get(0);
            game.movePlayer("X", edge.getTo(), edge.getTicket());
            currentPos = edge.getTo();
        }
    }
}