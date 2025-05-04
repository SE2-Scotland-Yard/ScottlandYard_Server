package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.board.Board;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AllowedNextMovesTest {

    /*
    private Board board;
    private Detective detective;

    @BeforeEach
    void setUp() {
        board = new Board();
        detective = new Detective();
    //    detective.pos = 128; //Startposition
    }


    @Test
    void testAllowedMovesFrom128_WithUndergroundTickets() {
        List<Integer> allowed = detective.allowedNextMoves(board);


        assertTrue(allowed.contains(142)); // taxi + bus
        assertTrue(allowed.contains(135)); // bus
        assertTrue(allowed.contains(185)); // underground
        assertTrue(allowed.contains(89));  // underground
        assertTrue(allowed.contains(140)); // underground


        assertFalse(allowed.contains(1));// nicht erlaubt
    }

    @Test
    void testAllowedMovesFrom128_WithoutUndergroundTickets() {

        detective.ticketsLeft.put(Transport.underground, 0);

        List<Integer> allowed = detective.allowedNextMoves(board);


        assertTrue(allowed.contains(142)); // taxi + bus
        assertTrue(allowed.contains(135)); // bus
        assertFalse(allowed.contains(185)); // underground
        assertFalse(allowed.contains(89));  // underground
        assertFalse(allowed.contains(140)); // underground
    }
*/
}
