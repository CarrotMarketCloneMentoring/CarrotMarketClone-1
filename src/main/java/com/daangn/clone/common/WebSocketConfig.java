package com.daangn.clone.common;

import com.daangn.clone.common.websocket.SocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketHandler socketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        // WebSocket 접속을 위한 EndPoint
        // => ws://localhost:8080/ws/connect
        registry.addHandler(socketHandler, "ws/connect").setAllowedOrigins("*");
    }
}
