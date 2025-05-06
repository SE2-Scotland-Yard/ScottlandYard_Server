package at.aau.serg.scotlandyard.gamelogic;

import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;


class RoundManagerTest {

    @Mock
    private Detective detective1;

    @Mock
    private Detective detective2;

    @Mock
    private MrX mrX;

    private RoundManager roundManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        //Setup Roundmanager
        ArrayList<Detective>detectives = new ArrayList<>();
        detectives.add(detective1);
        detectives.add(detective2);
        roundManager = new RoundManager(detectives,mrX);

        //Setup positions
        when(detective1.getPosition()).thenReturn(4);
        when(mrX.getPosition()).thenReturn(5);

    }

    @Test
    void testGetCurrentPlayer(){
        Player player = roundManager.getCurrentPlayer();
        assertEquals(mrX, player);
        roundManager.nextTurn();
        player = roundManager.getCurrentPlayer();
        assertEquals(detective1, player);
    }
    @Test
    void testNextTurn(){
        roundManager.nextTurn();
        assertEquals(detective1, roundManager.getCurrentPlayer());
        roundManager.nextTurn();
        assertEquals(detective2, roundManager.getCurrentPlayer());
        roundManager.nextTurn();
        assertEquals(mrX, roundManager.getCurrentPlayer());
    }

    @Test
    void testIsMrXVisible(){
        roundManager = new RoundManager(new ArrayList<>(), mrX);
        roundManager.nextTurn();
        roundManager.nextTurn();
        assertTrue(roundManager.isMrXVisible());

        roundManager = new RoundManager(new ArrayList<>(), mrX);
        roundManager.nextTurn();
        roundManager.nextTurn();
        roundManager.nextTurn();
        assertFalse(roundManager.isMrXVisible());
    }

    @Test
    void testIsMrXCaptured(){
        assertFalse(roundManager.isMrXCaptured());
        when(detective1.getPosition()).thenReturn(5);
        assertTrue(roundManager.isMrXCaptured());
    }

    @Test
    void testIsGameOverMrXCaptured(){
        assertFalse(roundManager.isGameOver());
        when(detective1.getPosition()).thenReturn(5);
        assertTrue(roundManager.isGameOver());
    }

    @Test
    void testIsGameOverMaxRounds(){
        assertFalse(roundManager.isGameOver());

        int totalTurns = 3 * 25; // MaxRounds = 24 (smaller than 25), 3 total Players

        for (int i = 0; i < totalTurns; i++) {
            roundManager.nextTurn();
        }

        assertTrue(roundManager.isGameOver());
    }

}
