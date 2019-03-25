package com.virtuace.groupchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;

@Service
public class WordsCounterUpdateService {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ConcurrentMap<String, Long> wordCountersUpdates;

    @Autowired
    public WordsCounterUpdateService(SimpMessageSendingOperations messagingTemplate, ConcurrentMap<String, Long> wordCountersUpdates) {
        this.messagingTemplate = messagingTemplate;
        this.wordCountersUpdates = wordCountersUpdates;
    }

    @Scheduled(fixedDelay = 500)
    public void sendUpdate() {
        synchronized (wordCountersUpdates) {
            messagingTemplate.convertAndSend("/topic/counters-update", wordCountersUpdates);
            wordCountersUpdates.clear();
        }
    }
}
