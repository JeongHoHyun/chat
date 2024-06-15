package com.websocket.chat.ctrl;

import com.websocket.chat.vo.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // 메시지를 처리하고, 필요한 경우 예외 처리를 추가
        try {
            chatMessage.setContent(HtmlUtils.htmlEscape(chatMessage.getContent()));
            return chatMessage;
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            return new ChatMessage(); // 빈 메시지 반환 (또는 적절한 예외 처리)
        }
    }
}
