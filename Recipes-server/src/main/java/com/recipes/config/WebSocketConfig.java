package com.recipes.config;


import com.recipes.handler.NotificationHandler;
import com.recipes.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final NotificationHandler notificationHandler;
    private final WebSocketHandshakeInterceptor handshakeInterceptor;

    public WebSocketConfig(NotificationHandler notificationHandler, WebSocketHandshakeInterceptor handshakeInterceptor) {
        this.notificationHandler = notificationHandler;
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(notificationHandler, "/ws/notification")
                .setAllowedOrigins("http://localhost:3000") // 你的前端地址
                .addInterceptors(handshakeInterceptor);

        registry.addHandler(notificationHandler, "/ws/notification/*")
                .setAllowedOrigins("http://localhost:8082") // 你的前端地址
                .addInterceptors(handshakeInterceptor);
    }
}
