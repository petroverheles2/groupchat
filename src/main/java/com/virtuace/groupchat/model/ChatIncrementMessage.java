package com.virtuace.groupchat.model;

import lombok.Data;

import java.util.List;

@Data
public class ChatIncrementMessage extends ChatMessage {
    private List<String> increments;
}
