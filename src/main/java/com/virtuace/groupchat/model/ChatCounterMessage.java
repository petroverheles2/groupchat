package com.virtuace.groupchat.model;

import lombok.Data;

import java.util.Map;

@Data
public class ChatCounterMessage extends ChatMessage {
    private Map<String, Long> counters;
}
