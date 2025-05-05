package at.aau.serg.scotlandyard.gamelogic.Tickets;

import at.aau.serg.scotlandyard.gamelogic.player.tickets.PlayerTickets;
import at.aau.serg.scotlandyard.gamelogic.player.tickets.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTicketsTest {
    private PlayerTickets playerTickets;
    private final Map<Ticket, Integer> initialTickets = new EnumMap<>(Ticket.class);


    @BeforeEach
    void setUp() {
        initialTickets.put(Ticket.taxi, 4);
        initialTickets.put(Ticket.bus, 3);
        initialTickets.put(Ticket.underground, 2);
        initialTickets.put(Ticket.BLACK, 1);
        initialTickets.put(Ticket.DOUBLE,0);
        playerTickets = new PlayerTickets(initialTickets);
    }


    @Test
    void hasTicketTestAvailable(){
        assertTrue(playerTickets.hasTicket(Ticket.taxi));
        assertTrue(playerTickets.hasTicket(Ticket.bus));
        assertTrue(playerTickets.hasTicket(Ticket.underground));
        assertTrue(playerTickets.hasTicket(Ticket.BLACK));

    }

    @Test
    void hasTicketTestNotAvailable(){
        assertFalse(playerTickets.hasTicket(Ticket.DOUBLE));
    }

    @Test
    void useTicketTest(){
        playerTickets.useTicket(Ticket.taxi);
        assertEquals(3, playerTickets.getTicketCount(Ticket.taxi));
    }

    @Test
    void useTicket_ThrowExceptionWhenNoTicketsLeft() {
        assertThrows(IllegalStateException.class, () -> playerTickets.useTicket(Ticket.DOUBLE));
    }

    @Test
    void addTicket_shouldIncreaseTicketCount() {
        playerTickets.addTicket(Ticket.taxi);
        assertEquals(5, playerTickets.getTicketCount(Ticket.taxi));

        playerTickets.addTicket(Ticket.DOUBLE);
        assertEquals(1, playerTickets.getTicketCount(Ticket.DOUBLE));
    }

    @Test
    void getTicketCount() {
        assertEquals(4, playerTickets.getTicketCount(Ticket.taxi));
        assertEquals(3, playerTickets.getTicketCount(Ticket.bus));
        assertEquals(2, playerTickets.getTicketCount(Ticket.underground));
        assertEquals(1, playerTickets.getTicketCount(Ticket.BLACK));
        assertEquals(0, playerTickets.getTicketCount(Ticket.DOUBLE));
    }


}
