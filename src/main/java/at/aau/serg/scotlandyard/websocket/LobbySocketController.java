package at.aau.serg.scotlandyard.websocket;

import at.aau.serg.scotlandyard.dto.ReadyMessage;
import at.aau.serg.scotlandyard.dto.LobbyState;
import at.aau.serg.scotlandyard.dto.LobbyMapper;
import at.aau.serg.scotlandyard.gamelogic.*;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.Player;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LobbySocketController {

    private final LobbyManager lobbyManager;
    private final GameManager gameManager;
    private final SimpMessagingTemplate messaging;

    public LobbySocketController(LobbyManager lobbyManager, GameManager gameManager, SimpMessagingTemplate messaging) {
        this.lobbyManager = lobbyManager;
        this.gameManager = gameManager;
        this.messaging = messaging;
    }

    @MessageMapping("/lobby/ready")
    public void handleReady(ReadyMessage msg) {
        String gameId = msg.getGameId();
        String player = msg.getPlayerId();

        Lobby lobby = lobbyManager.getLobby(gameId);
        if (lobby == null) return;

        // Spieler als ready markieren
        lobby.markReady(player);

        //aktueller Lobby-Zustand
        LobbyState state = LobbyMapper.toLobbyState(lobby);
        messaging.convertAndSend("/topic/lobby/" + gameId, state);

        // Wenn alle ready → Spiel starten
        if (lobby.allReady() && lobby.hasEnoughPlayers() && !lobby.isStarted()) {
            lobby.markStarted();

            GameState game = gameManager.getOrCreateGame(gameId);
            List<String> playerNames = new ArrayList<>(lobby.getPlayers());
            for (String name : playerNames) {
                Role role = lobby.getSelectedRole(name);
                Player p = (role == Role.MRX) ? new MrX() : new Detective();
                game.addPlayer(name, p);
            }



            // Spiel gestartet
            LobbyState startedState = LobbyMapper.toLobbyState(lobby);
            messaging.convertAndSend("/topic/lobby/" + gameId, startedState);
        }
    }
}
