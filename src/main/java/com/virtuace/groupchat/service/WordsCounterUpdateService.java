package com.virtuace.groupchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WordsCounterUpdateService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final Map<String, AtomicLong> wordCounters;

    @Autowired
    public WordsCounterUpdateService(SimpMessageSendingOperations messagingTemplate, Map<String, AtomicLong> wordCounters) {
        this.messagingTemplate = messagingTemplate;
        this.wordCounters = wordCounters;
    }

    @Async
    public void sendUpdate(Iterable<String> words) {
        if (words == null) {
            return;
        }

        Map<String, Long> counterUpdates = new HashMap<>();
        for (String word : words) {
            counterUpdates.put(word, wordCounters.get(word).longValue());
        }

        messagingTemplate.convertAndSend("/topic/counters-update", counterUpdates);
    }
}
