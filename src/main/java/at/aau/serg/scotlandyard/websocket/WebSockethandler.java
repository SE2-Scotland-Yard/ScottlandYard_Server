package at.aau.serg.scotlandyard.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSockethandler extends TextWebSocketHandler {
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final Logger logger = LoggerFactory.getLogger(WebSockethandler.class);

    private static final int MIN_PLAYERS = 3;
    private static final int MAX_PLAYERS = 6;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        String validatedId = validateSessionId(session.getId());
        broadcastMessage("Player joined: " + validatedId);
        logger.info("Player connected: {}", validatedId);
        checkAndStartGame();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String validatedId = validateSessionId(session.getId());
        logger.info("Received message from {}", validatedId);
        broadcastMessage("Player " + validatedId + ": message received");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        String validatedId = validateSessionId(session.getId());
        broadcastMessage("Player left: " + validatedId);
        logger.info("Player disconnected: {}", validatedId);
    }

    private void broadcastMessage(String message) {
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                logger.error("Error sending message to {}: {}", validateSessionId(session.getId()), e.getMessage());
            }
        }
    }

    private void checkAndStartGame() {
        if (sessions.size() >= MIN_PLAYERS && sessions.size() <= MAX_PLAYERS) {
            startGame();
        } else if (sessions.size() > MAX_PLAYERS) {
            logger.info("Maximum player count reached. Game is starting.");
            broadcastMessage("Maximum player count reached. Game is starting.");
            startGame();
        }
    }

    private void startGame() {
        logger.info("Starting game with {} players.", sessions.size());
        broadcastMessage("Game is starting!");
        assignPlayerRoles();
        initializeGameState();
        startTheGame();
    }

    private void assignPlayerRoles() {
        logger.info("Assigning player roles.");
        broadcastMessage("Assigning player roles!");
    }

    private void initializeGameState() {
        logger.info("Initializing game state.");
        broadcastMessage("Initializing game state.");
    }

    private void startTheGame() {
        logger.info("Game has started.");
        broadcastMessage("Game has started.");
    }

    public String validateSessionId(String sessionId) {
        return sessionId.replaceAll("[\n\r]", "_");
        }
}