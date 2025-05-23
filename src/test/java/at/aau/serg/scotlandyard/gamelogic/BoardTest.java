package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testConnectionsFromNode1() {
        List<Edge> edges = board.getConnectionsFrom(1);
        assertNotNull(edges);
        assertEquals(5, edges.size(), "Node 1 should have 5 edges");

        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 8 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 9 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 46 && e.getTicket() == Ticket.BUS));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 58 && e.getTicket() == Ticket.BUS));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 46 && e.getTicket() == Ticket.UNDERGROUND));
    }

    @Test
    void testConnectionsFromNode32() {
        List<Edge> edges = board.getConnectionsFrom(32);
        assertNotNull(edges);
        assertEquals(4, edges.size(), "Node 32 should have 4 edges");

        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 19 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 33 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 44 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 45 && e.getTicket() == Ticket.TAXI));
    }

    @Test
    void testConnectionsFromNode146() {
        List<Edge> edges = board.getConnectionsFrom(146);
        assertNotNull(edges);
        assertEquals(4, edges.size(), "Node 146 should have 4 edges");

        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 145 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 147 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 163 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 122 && e.getTicket() == Ticket.TAXI));
    }

    @Test
    void testConnectionsFromNode128() {
        List<Edge> edges = board.getConnectionsFrom(128);
        assertNotNull(edges);
        assertEquals(13, edges.size());

        // Test auf ein paar gezielte Verbindungen
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 142 && e.getTicket() == Ticket.TAXI));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 135 && e.getTicket() == Ticket.BUS));
        assertTrue(edges.stream().anyMatch(e -> e.getTo() == 140 && e.getTicket() == Ticket.UNDERGROUND));
    }

    @Test
    void testConnectionsFromNonExistingNodeReturnsEmpty() {
        List<Edge> edges = board.getConnectionsFrom(999); // nicht erlaubterKnoten
        assertNotNull(edges);
        assertTrue(edges.isEmpty(), "Es sollten keine Verbindungen für einen ungültigen Knoten vorhanden sein");
    }


    @Test
    void testEdgeProperties() {
        List<Edge> edges = board.getConnectionsFrom(1);
        assertNotNull(edges);
        boolean found = edges.stream().anyMatch(e -> e.getTo() > 0 && e.getTicket() != null);
        assertTrue(found, "Mindestens eine Kante sollte ein Ziel und ein Ticket haben");
    }

}
