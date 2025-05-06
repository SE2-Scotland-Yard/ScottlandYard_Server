package at.aau.serg.scotlandyard.gamelogic;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameManagerTest {

    @Test
    void getOrCreateGame_newGame() {
        GameManager gameManager = new GameManager();
        String gameId = "TESTGAME1";
        GameState gameState = gameManager.getOrCreateGame(gameId);
        assertNotNull(gameState);
        assertTrue(gameManager.getAllGameIds().contains(gameId));
    }

    @Test
    void getOrCreateGame_existingGame() {
        GameManager gameManager = new GameManager();
        String gameId = "TESTGAME2";
        GameState gameState1 = gameManager.getOrCreateGame(gameId);
        GameState gameState2 = gameManager.getOrCreateGame(gameId);
        assertNotNull(gameState1);
        assertSame(gameState1, gameState2);
        assertTrue(gameManager.getAllGameIds().contains(gameId));
    }

    @Test
    void getGame_existingGame() {
        GameManager gameManager = new GameManager();
        String gameId = "TESTGAME3";
        GameState createdState = gameManager.getOrCreateGame(gameId);
        GameState retrievedState = gameManager.getGame(gameId);
        assertNotNull(retrievedState);
        assertSame(createdState, retrievedState);
    }

    @Test
    void getGame_nonExistingGame() {
        GameManager gameManager = new GameManager();
        GameState retrievedState = gameManager.getGame("NONEXISTING");
        assertNull(retrievedState);
    }

    @Test
    void removeGame_existingGame() {
        GameManager gameManager = new GameManager();
        String gameId = "TESTGAME4";
        gameManager.getOrCreateGame(gameId);
        assertTrue(gameManager.getAllGameIds().contains(gameId));
        gameManager.removeGame(gameId);
        assertFalse(gameManager.getAllGameIds().contains(gameId));
        assertNull(gameManager.getGame(gameId));
    }

    @Test
    void removeGame_nonExistingGame() {
        GameManager gameManager = new GameManager();
        gameManager.removeGame("ANOTHERNONEXISTING"); // Sollte keine Fehler verursachen
        assertFalse(gameManager.getAllGameIds().contains("ANOTHERNONEXISTING"));
    }

    @Test
    void getAllGameIds_multipleGames() {
        GameManager gameManager = new GameManager();
        gameManager.getOrCreateGame("GAME_A");
        gameManager.getOrCreateGame("GAME_B");
        gameManager.getOrCreateGame("GAME_C");
        Set<String> allGameIds = gameManager.getAllGameIds();
        assertEquals(3, allGameIds.size());
        assertTrue(allGameIds.contains("GAME_A"));
        assertTrue(allGameIds.contains("GAME_B"));
        assertTrue(allGameIds.contains("GAME_C"));
    }

    @Test
    void getAllGameIds_noGames() {
        GameManager gameManager = new GameManager();
        Set<String> allGameIds = gameManager.getAllGameIds();
        assertTrue(allGameIds.isEmpty());
    }
}