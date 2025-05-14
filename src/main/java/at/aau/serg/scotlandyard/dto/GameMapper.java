package at.aau.serg.scotlandyard.dto;

import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.Player;

import java.util.HashMap;
import java.util.Map;

public class GameMapper {

    private GameMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static GameUpdate mapToGameUpdate(String gameId, Map<String, Player> playerMap) {
        Map<String, Integer> playerPositions = new HashMap<>();

        for (Map.Entry<String, Player> entry : playerMap.entrySet()) {
            String name = entry.getKey();
            Player player = entry.getValue();
            if(!(player instanceof MrX)) {
                playerPositions.put(name, player.getPosition());
            }
        }

        return new GameUpdate(gameId, playerPositions);
    }
}
