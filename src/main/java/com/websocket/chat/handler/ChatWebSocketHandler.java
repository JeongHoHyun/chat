package com.websocket.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = Logger.getLogger(ChatWebSocketHandler.class.getName());
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        logger.info("WebSocket connection established with session: " + session.getId());
    }

    public void broadcastMessage(String message) throws Exception {
        logger.info("Broadcasting message: " + message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                logger.info("Sending message to session: " + session.getId());
                session.sendMessage(new TextMessage(message));
            } else {
                logger.warning("Session is closed: " + session.getId());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        logger.info("WebSocket connection closed with session: " + session.getId());
    }
}
