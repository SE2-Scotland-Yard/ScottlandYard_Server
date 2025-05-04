package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMovementTest {

    private Board board;

    @BeforeEach
    void setup() {
        board = new Board();
    }

    @Test
    void testValidMove_ShouldUpdatePositionAndUseTicket() {
        Board board = new Board();
        Detective detective = new Detective();
        int from = detective.getPosition();
        List<Edge> connections = board.getConnectionsFrom(from);

        Edge validEdge = connections.stream()
                .filter(e -> detective.getTickets().hasTicket(e.getTicket()))
                .findFirst().orElseThrow();

        detective.move(validEdge.getTo(), validEdge.getTicket(), board);

        assertEquals(validEdge.getTo(), detective.getPosition());
        assertEquals(9, detective.getTickets().getTicketCount(validEdge.getTicket())); // initial - 1
    }

    @Test
    void testInvalidMove_ShouldThrowException() {
        Board board = new Board();
        Detective detective = new Detective();

        assertThrows(IllegalArgumentException.class, () ->
                detective.move(999, Ticket.TAXI, board)); // ungültiges Ziel
    }

    @Test
    void testMovePlayer_ValidMove_ShouldReturnTrue() {
        GameState game = new GameState();
        Detective det = new Detective();
        game.addPlayer("Alice", det);

        int from = det.getPosition();
        Board board = game.getBoard();
        int to = board.getConnectionsFrom(from).stream()
                .map(Edge::getTo)
                .findFirst().orElseThrow();

        boolean moved = game.movePlayer("Alice", to, Ticket.TAXI);
        assertTrue(moved);
        assertEquals(to, det.getPosition());
    }

    @Test
    void testMovePlayer_InvalidTarget_ShouldReturnFalse() {
        GameState game = new GameState();
        Detective det = new Detective();
        game.addPlayer("Bob", det);

        boolean moved = game.movePlayer("Bob", 999, Ticket.TAXI);
        assertFalse(moved);
    }

    @Test
    void testVisibleMrXPosition_ShouldReturnPositionOnRevealRound() {
        GameState game = new GameState();
        MrX mrX = new MrX();
        game.addPlayer("X", mrX);

        int start = mrX.getPosition();
        Board board = game.getBoard();

        // Erster gültiger Zug
        int firstMove = board.getConnectionsFrom(start).get(0).getTo();
        Ticket firstTicket = board.getConnectionsFrom(start).get(0).getTicket();
        game.movePlayer("X", firstMove, firstTicket); // Runde 1

        // Zweiter gültiger Zug
        int secondMove = board.getConnectionsFrom(firstMove).get(0).getTo();
        Ticket secondTicket = board.getConnectionsFrom(firstMove).get(0).getTicket();
        game.movePlayer("X", secondMove, secondTicket); // Runde 2

        // Dritter gültiger Zug (worauf getestet wird testen)
        int thirdMove = board.getConnectionsFrom(secondMove).get(0).getTo();
        Ticket thirdTicket = board.getConnectionsFrom(secondMove).get(0).getTicket();
        game.movePlayer("X", thirdMove, thirdTicket); // Runde 3 = Sichtbar

        String visible = game.getVisibleMrXPosition();
        assertEquals(String.valueOf(thirdMove), visible);
    }


    @Test
    void testInvisibleMrXPosition_ShouldReturnQuestionMark() {
        GameState game = new GameState();
        MrX mrX = new MrX();
        game.addPlayer("X", mrX);

        int to = game.getBoard().getConnectionsFrom(mrX.getPosition()).get(0).getTo();
        game.movePlayer("X", to, Ticket.TAXI); // Runde 1

        String visible = game.getVisibleMrXPosition();
        assertEquals("?", visible);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 5, 6, 7}) // unsichtbare Runden
    void testMrXShouldBeInvisibleInTheseRounds(int roundsToMove) {
        GameState game = new GameState();
        MrX mrX = new MrX();
        game.addPlayer("X", mrX);
        Board board = game.getBoard();

        int currentPos = mrX.getPosition();

        for (int i = 1; i <= roundsToMove; i++) {
            List<Edge> connections = board.getConnectionsFrom(currentPos);
            Edge edge = connections.get(0);

            game.movePlayer("X", edge.getTo(), edge.getTicket());
            currentPos = edge.getTo();
        }

        // Prüft Sichtbarkeit NACH dem letzten Move
        if (!game.getRevealRounds().contains(roundsToMove)) {
            String visible = game.getVisibleMrXPosition();
            assertEquals("?", visible, "MrX sollte in Runde " + roundsToMove + " unsichtbar sein!");
        }
    }

    @Test
    void testMrXDoubleMove_ShouldExecuteTwoMovesAndStoreHistory() {
        GameState game = new GameState();
        MrX mrX = new MrX();
        game.addPlayer("X", mrX);

        Board board = game.getBoard();
        int from = mrX.getPosition();
        Edge first = board.getConnectionsFrom(from).get(0);

        int mid = first.getTo();
        Edge second = board.getConnectionsFrom(mid).get(0);

        boolean moved = game.moveMrXDouble("X", first.getTo(), first.getTicket(), second.getTo(), second.getTicket());
        assertTrue(moved);
        assertEquals(second.getTo(), mrX.getPosition());
        assertEquals(2, game.getMrXMoveHistory().size());
    }

}

