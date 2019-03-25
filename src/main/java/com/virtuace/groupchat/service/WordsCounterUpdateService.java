package com.virtuace.groupchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WordsCounterUpdateService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final Set<String> wordCountersUpdates;
    private final Map<String, AtomicLong> wordCounters;

    @Autowired
    public WordsCounterUpdateService(SimpMessageSendingOperations messagingTemplate, Set<String> wordCountersUpdates, Map<String, AtomicLong> wordCounters) {
        this.messagingTemplate = messagingTemplate;
        this.wordCountersUpdates = wordCountersUpdates;
        this.wordCounters = wordCounters;
    }

    @Scheduled(fixedDelay = 500)
    public void sendUpdate() {
        Map<String, Long> counterUpdates = new HashMap<>();
        for (String word : wordCountersUpdates) {
            counterUpdates.put(word, wordCounters.get(word).longValue());
        }
        messagingTemplate.convertAndSend("/topic/counters-update", counterUpdates);
        wordCountersUpdates.clear();
    }
}
