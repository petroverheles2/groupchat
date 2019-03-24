package com.virtuace.groupchat.model;

import lombok.Data;

@Data
public class ChatIncrementMessage extends ChatMessage {
    private Iterable<String> increments;
}
