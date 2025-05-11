package at.aau.serg.scotlandyard.dto;

import at.aau.serg.scotlandyard.gamelogic.RoundManager;
import at.aau.serg.scotlandyard.gamelogic.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameMapper {

   /* private GameMapper(RoundManager roundManager) {

        Map<Player, Integer> playerPositions = new HashMap<>();
        for (var entry : roundManager.getTurnOrder()) {
            playerPositions.put(entry.getKey(), entry.getValue().getPosition());
        }


        return new GameUpdate(
                lobby.getGameId(),
                new ArrayList<>(lobby.getPlayers()),
                new HashMap<>(lobby.getReadyStatus()),
                roleMap,
                lobby.isPublic(),
                lobby.isStarted(),
                6
        );
    }

    */
}
