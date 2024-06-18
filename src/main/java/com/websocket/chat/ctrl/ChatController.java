package com.websocket.chat.ctrl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.chat.handler.ChatWebSocketHandler;
import com.websocket.chat.vo.ChatMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class ChatController {

    private static final Logger logger = Logger.getLogger(ChatController.class.getName());
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatController(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @PostMapping("/sendMessage")
    public @ResponseBody Map<String, String> sendMessage(@RequestBody ChatMessage chatMessage) throws Exception {
        logger.info("Received message: " + chatMessage.getContent());

        // 서버에서 추가할 데이터
        chatMessage.setSender("ServerAssignedUserId"); // 예: 실제 사용자의 ID
        chatMessage.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // JSON 형식으로 변환
        String message = objectMapper.writeValueAsString(chatMessage);

        // WebSocket을 통해 메시지 브로드캐스트
        try {
            chatWebSocketHandler.broadcastMessage(message);
            logger.info("Message broadcasted successfully");
        } catch (Exception e) {
            logger.severe("Failed to broadcast message: " + e.getMessage());
            throw e;
        }

        // 응답으로 성공 메시지 반환
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }
}
