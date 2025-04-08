package at.aau.serg.scotlandyard.websocket;

import at.aau.serg.scotlandyard.websocket.WebSockethandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import static org.mockito.Mockito.*;
//import org.mockito.ArgumentMatcher;
import static org.mockito.ArgumentMatchers.argThat;
class WebSockethandlerTest {

    private WebSockethandler handler;
    private WebSocketSession session1;
    private WebSocketSession session2;

    @BeforeEach
    void setUp() {
        handler = new WebSockethandler();
        session1 = mock(WebSocketSession.class);
        session2 = mock(WebSocketSession.class);

        when(session1.getId()).thenReturn("session1");
        when(session2.getId()).thenReturn("session2");
        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);
    }

    
    @Test
    void testAfterConnectionEstablished_AddsSessionAndBroadcasts() throws Exception {
        handler.afterConnectionEstablished(session1);

        // Später gesendete Nachricht muss "Player joined" enthalten
        verify(session1, atLeastOnce()).sendMessage(argThat((TextMessage msg) ->
                msg.getPayload().contains("Player joined: session1")
        ));
    }

    @Test
    void testHandleTextMessage_BroadcastsReceivedMessage() throws Exception {
        handler.afterConnectionEstablished(session1);

        TextMessage msg = new TextMessage("Hello from player!");
        handler.handleTextMessage(session1, msg);

        verify(session1, atLeastOnce()).sendMessage(argThat((TextMessage sent) ->
                sent.getPayload().contains("message received")
        ));
    }


    @Test
    void testAfterConnectionClosed_RemovesSessionAndBroadcasts() throws Exception {
        // Arrange
        WebSocketSession session2 = mock(WebSocketSession.class);
        when(session1.getId()).thenReturn("session1");
        when(session2.getId()).thenReturn("session2");

        when(session1.isOpen()).thenReturn(true);
        when(session2.isOpen()).thenReturn(true);

        handler.afterConnectionEstablished(session1);
        handler.afterConnectionEstablished(session2);

        // Act
        handler.afterConnectionClosed(session1, null);

        // Assert
        verify(session2, atLeastOnce()).sendMessage(argThat((TextMessage msg) ->
                msg.getPayload().contains("Player left: session1")
        ));
    }


    @Test
    void testCheckAndStartGame_StartsGameWhenEnoughPlayers() throws Exception {
        // Add drei Sessions, um das Spiel zu starten
        WebSocketSession session3 = mock(WebSocketSession.class);
        when(session3.getId()).thenReturn("session3");
        when(session3.isOpen()).thenReturn(true);

        handler.afterConnectionEstablished(session1);
        handler.afterConnectionEstablished(session2);
        handler.afterConnectionEstablished(session3);

        // Alle Spieler sollten Nachricht über Spielstart erhalten
        verify(session1, atLeastOnce()).sendMessage(argThat((TextMessage msg) ->
                msg.getPayload().contains("Game is starting!")
        ));
        verify(session2, atLeastOnce()).sendMessage(argThat((TextMessage msg) ->
                msg.getPayload().contains("Game is starting!")
        ));
        verify(session3, atLeastOnce()).sendMessage(argThat((TextMessage msg) ->
                msg.getPayload().contains("Game is starting!")
        ));
    }

    @Test
    void testValidateSessionId_RemovesCRLF() {
        String result = handler.validateSessionId("abc\r\ndef");
        assert result.equals("abc__def");
    }
}