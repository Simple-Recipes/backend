package com.recipes.handler;




import com.recipes.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class NotificationHandler extends TextWebSocketHandler {

    private static CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public NotificationHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        UserDTO user = (UserDTO) session.getAttributes().get("user");
        if (user != null) {
            String userMessage = "User " + user.getUsername() + ": " + message.getPayload();
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/notifications", userMessage);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    public void sendNotification(String message) {
        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
