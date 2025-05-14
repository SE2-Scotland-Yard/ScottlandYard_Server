package at.aau.serg.scotlandyard.websocket;

import at.aau.serg.scotlandyard.dto.*;
import at.aau.serg.scotlandyard.gamelogic.*;
import at.aau.serg.scotlandyard.gamelogic.player.Detective;
import at.aau.serg.scotlandyard.gamelogic.player.MrX;
import at.aau.serg.scotlandyard.gamelogic.player.Player;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

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
        Lobby lobby = lobbyManager.getLobby(gameId);
        if (lobby == null) return;

        lobby.markReady(msg.getPlayerId());
        messaging.convertAndSend("/topic/lobby/" + gameId, LobbyMapper.toLobbyState(lobby));

        if (lobby.allReady() && lobby.hasEnoughPlayers() && !lobby.isStarted()) {
            lobby.markStarted();

            messaging.convertAndSend("/topic/lobby/" + gameId, LobbyMapper.toLobbyState(lobby));//lobbyUpdate
            GameState game = initializeGame(gameId, lobby); // Spiel initialisieren

        }
    }
    private GameState initializeGame(String gameId, Lobby lobby) {
        GameState game = gameManager.getOrCreateGame(gameId);
        List<Detective> detectives = new ArrayList<>();
        MrX mrX = null;

        for (String playerName : lobby.getPlayers()) {
            Role role = lobby.getSelectedRole(playerName);
            Player player = (role == Role.MRX) ? new MrX(playerName) : new Detective(playerName);
            game.addPlayer(playerName, player);

            if (role == Role.MRX) {
                mrX = (MrX) player;
            } else {
                detectives.add((Detective) player);
            }
        }

        if (!detectives.isEmpty()) {
            game.initRoundManager(detectives, mrX); // RoundManager korrekt initialisieren

            //GameUpdate update = GameMapper.mapToGameUpdate(gameId, game.getAllPlayers());

            System.out.println("Sending GameUpdate to /topic/game/" + gameId);
            //System.out.println("GameUpdate payload: " + new Gson().toJson(update));
            System.out.println("Aktueller Spieler im Mapper: " + game.getCurrentPlayerName());
            messaging.convertAndSend("/topic/game/" + gameId, GameMapper.mapToGameUpdate(gameId, game.getAllPlayers(), game.getCurrentPlayerName()));//positionen

        }

        return game;
    }

    @MessageMapping("/lobby/role")
    public void selectRole(RoleSelectionMessage msg) {
        Lobby lobby = lobbyManager.getLobby(msg.getGameId());
        if (lobby != null) {
            lobby.selectRole(msg.getPlayerId(), msg.getRole());
            messaging.convertAndSend("/topic/lobby/" + msg.getGameId(), LobbyMapper.toLobbyState(lobby));
        }
    }

    @MessageMapping("/game/requestOwnPosition")
    public void handleOwnPositionRequest(Map<String, String> request) {
        String gameId = request.get("gameId");
        String playerId = request.get("playerId");

        GameState game = gameManager.getGame(gameId);
        if (game == null) return;

        Player player = game.getAllPlayers().get(playerId);
        if (player != null) {
            int position = player.getPosition();

            messaging.convertAndSend("/topic/ownPosition/" + playerId, Map.of("position", position));




            System.out.println("→ Eigene Position an MrX gesendet: " + position);
        }
    }




    @MessageMapping("/lobby/game/update")
    public void gameUpdate(String gameId, GameState game) {

       // messaging.convertAndSend("/topic/game/update" + gameId, GameMapper.mapToGameUpdate(gameId, game.getAllPlayers()));
    }



}
