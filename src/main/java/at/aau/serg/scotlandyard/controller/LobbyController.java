package at.aau.serg.scotlandyard.controller;

import at.aau.serg.scotlandyard.dto.LobbyUpdate;
import at.aau.serg.scotlandyard.gamelogic.Lobby;
import at.aau.serg.scotlandyard.gamelogic.LobbyManager;
import at.aau.serg.scotlandyard.dto.LobbyState;
import at.aau.serg.scotlandyard.dto.LobbyMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private final LobbyManager lobbyManager;
    private final SimpMessagingTemplate messaging;



    public LobbyController(LobbyManager lobbyManager, SimpMessagingTemplate messaging) {
        this.lobbyManager = lobbyManager;
        this.messaging = messaging;
    }

    @PostMapping("/create")
    public LobbyState createLobby(
            @RequestParam boolean isPublic,
            @RequestParam String name
    ) {
        // 1) Lobby anlegen
        Lobby lobby = lobbyManager.createLobby(isPublic);
        // 2) Ersteller direkt als Spieler hinzufügen
        lobby.addPlayer(name);

        // 3)  Zustand broadcasten
        LobbyState state = LobbyMapper.toLobbyState(lobby);
        messaging.convertAndSend("/topic/lobby/" + lobby.getGameId(), state);

        // 4) State zurück an den Aufrufer
        return state;
    }


    @PostMapping("/{gameId}/join")
    public ResponseEntity<String> joinLobby(@PathVariable String gameId,
                                            @RequestParam String name) {
        Lobby lobby = lobbyManager.getLobby(gameId);
        if (lobby == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lobby mit der ID " + gameId + " wurde nicht gefunden.");
        }

        boolean success = lobby.addPlayer(name);
        if (!success) {
            return ResponseEntity.badRequest()
                    .body("Lobby ist voll oder bereits gestartet.");
        }

        System.out.println("WS-Broadcast: /topic/lobby/" + gameId);
        System.out.println("Spieler in der Lobby: " + lobby.getPlayers());


        LobbyState state = LobbyMapper.toLobbyState(lobby);
        messaging.convertAndSend("/topic/lobby/" + gameId, state);

        return ResponseEntity.ok(name + " ist der Lobby " + gameId + " beigetreten.");
    }

    @PostMapping("/{gameId}/leave")
    public ResponseEntity<String> leaveLobby(
            @PathVariable String gameId,
            @RequestParam String name
    ) {
        Lobby lobby = lobbyManager.getLobby(gameId);
        if (lobby == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lobby nicht gefunden");
        }
        lobby.removePlayer(name);
        // Wenn keine Spieler mehr drin sind, löschen
        if (lobby.getPlayers().isEmpty()) {
            lobbyManager.removeLobby(gameId);
        } else {
            // sonst Broadcast des neuen Zustands
            LobbyState state = LobbyMapper.toLobbyState(lobby);
            messaging.convertAndSend("/topic/lobby/" + gameId, state);
        }
        return ResponseEntity.ok(name + " hat die Lobby verlassen");
    }



    @GetMapping("/public")
    public List<LobbyState> getPublicLobbies() {
        return lobbyManager.getPublicLobbies().stream()
                .map(LobbyMapper::toLobbyState)
                .toList();
    }

    @GetMapping("/{gameId}/status")
    public ResponseEntity<LobbyState> getLobbyStatus(@PathVariable String gameId) {
        Lobby lobby = lobbyManager.getLobby(gameId);
        if (lobby == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LobbyMapper.toLobbyState(lobby));
    }
}
