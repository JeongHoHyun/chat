package com.websocket.chat.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String content;
    private String sender;
}
