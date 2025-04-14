package at.aau.serg.scotlandyard.dto;

import at.aau.serg.scotlandyard.gamelogic.Lobby;

import java.util.ArrayList;
import java.util.HashMap;

public class LobbyMapper {

    public static LobbyState toLobbyState(Lobby lobby) {
        return new LobbyState(
                lobby.getGameId(),
                new ArrayList<>(lobby.getPlayers()),
                new HashMap<>(lobby.getReadyStatus()),
                lobby.isPublic(),
                lobby.isStarted(),
                6
        );
    }
}
