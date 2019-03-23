package com.virtuace.groupchat.model;

import lombok.Data;

@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private long timestamp;
}
