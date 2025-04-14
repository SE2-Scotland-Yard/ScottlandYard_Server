package at.aau.serg.scotlandyard.gamelogic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Lobby {

    private final String gameId;
    private final Set<String> players = new HashSet<>();
    private final Map<String, Boolean> readyStatus = new HashMap<>();
    private final int minPlayers = 3;
    private final int maxPlayers = 6;
    private final boolean isPublic;
    private boolean started = false;

    public Lobby(String gameId, boolean isPublic) {
        this.gameId = gameId;
        this.isPublic = isPublic;
    }

    public boolean addPlayer(String name) {
        if (started || players.size() >= maxPlayers) return false;
        boolean added = players.add(name);
        if (added) {
            readyStatus.put(name, false); // standardmäßig nicht bereit
        }
        return added;
    }

    public boolean removePlayer(String name) {
        readyStatus.remove(name);
        return players.remove(name);
    }

    public boolean markReady(String name) {
        if (!players.contains(name)) return false;
        readyStatus.put(name, true);
        return true;
    }

    public boolean isPlayerReady(String name) {
        return readyStatus.getOrDefault(name, false);
    }

    public boolean allReady() {
        return !readyStatus.isEmpty() &&
                readyStatus.values().stream().allMatch(Boolean::booleanValue);
    }

    public Set<String> getPlayers() {
        return players;
    }

    public Map<String, Boolean> getReadyStatus() {
        return readyStatus;
    }

    public String getGameId() {
        return gameId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isStarted() {
        return started;
    }

    public void markStarted() {
        this.started = true;
    }

    public boolean hasEnoughPlayers() {

        if(players.size()>=minPlayers) {return true;}else{return false;}
    }
}

