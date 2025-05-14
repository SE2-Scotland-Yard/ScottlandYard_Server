package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.board.Edge;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
public class EdgeTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Edge edge = new Edge();
        edge.setTo(42);
        edge.setTransport(Ticket.BUS);

        assertEquals(42, edge.getTo());
        assertEquals(Ticket.BUS, edge.getTicket());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        Edge edge = new Edge(17, Ticket.UNDERGROUND);

        assertEquals(17, edge.getTo());
        assertEquals(Ticket.UNDERGROUND, edge.getTicket());
    }

    @Test
    void testToString() {
        Edge edge = new Edge(5, Ticket.TAXI);
        String str = edge.toString();
        assertTrue(str.contains("to=5"));
        assertTrue(str.contains("TAXI"));
    }

    @Test
    void testSettersChangeState() {
        Edge edge = new Edge(1, Ticket.BUS);
        edge.setTo(99);
        edge.setTransport(Ticket.UNDERGROUND);

        assertEquals(99, edge.getTo());
        assertEquals(Ticket.UNDERGROUND, edge.getTicket());
    }

}
