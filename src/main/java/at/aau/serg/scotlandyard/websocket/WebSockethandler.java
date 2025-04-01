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

    private static final int REQUIRED_PLAYERS = 4;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        broadcastMessage("Player joined: " + session.getId());
        logger.info("Player connected: {}", session.getId());
        checkAndStartGame();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("Received from {}: {}", session.getId(), message.getPayload());
        broadcastMessage("Player " + session.getId() + ": " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        broadcastMessage("Player left: " + session.getId());
        logger.info("Player disconnected: {}", session.getId());
    }

    private void broadcastMessage(String message) {
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                logger.error("Error sending message to {}: {}", session.getId(), e.getMessage());
            }
        }
    }

    private void checkAndStartGame() {
        if (sessions.size() == REQUIRED_PLAYERS) {
            startGame();
        }
    }

    private void startGame() {
        logger.info("Starting game with {} players.", REQUIRED_PLAYERS);
        broadcastMessage("Game is starting!");
        assignPlayerRoles();
        initializeGameState();
        startTheGame();
    }

    private void assignPlayerRoles() {
        logger.info("Assigning player roles.");
    }

    private void initializeGameState() {
        logger.info("Initializing game state.");
    }

    private void startTheGame() {
        logger.info("Game has started.");
    }
}