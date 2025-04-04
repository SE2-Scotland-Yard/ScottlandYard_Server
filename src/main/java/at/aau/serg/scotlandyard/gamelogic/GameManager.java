package at.aau.serg.scotlandyard.gamelogic;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class GameManager {

    private final Map<String, GameState> games = new HashMap<>();

    public GameState getOrCreateGame(String gameId) {
        return games.computeIfAbsent(gameId, id -> new GameState());
    }

    public GameState getGame(String gameId) {
        return games.get(gameId);
    }

    public void removeGame(String gameId) {
        games.remove(gameId);
    }

    public Set<String> getAllGameIds() {
        return games.keySet();
    }
}
