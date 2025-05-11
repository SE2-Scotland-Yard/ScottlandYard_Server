package at.aau.serg.scotlandyard.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp") // Client verbindet sich zu diesem Pfad
                .setAllowedOrigins("*");

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // Für Broadcasts
        config.setApplicationDestinationPrefixes("/app"); // Für Messages vom Client
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setSendTimeLimit(15 * 1000) // 15 Sekunden Timeout
                .setSendBufferSizeLimit(512 * 1024); // 512 KB Buffer
    }
}
