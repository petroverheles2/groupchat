package com.virtuace.groupchat.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@RunWith(MockitoJUnitRunner.class)
public class WordsCounterUpdateServiceTest {
    private WordsCounterUpdateService wordsCounterUpdateService;

    @Mock
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Mock
    private Set<String> wordCountersUpdates;

    @Mock
    private Map<String, AtomicLong> wordCounters;

    @Before
    public void setUp() {
        wordsCounterUpdateService = new WordsCounterUpdateService(simpMessageSendingOperations, wordCountersUpdates, wordCounters);
    }

    @Test
    public void sendUpdate() {
        wordsCounterUpdateService.sendUpdate();
        Mockito.verify(simpMessageSendingOperations).convertAndSend("/topic/counters-update", wordCountersUpdates);
        Mockito.verify(wordCountersUpdates).clear();
    }
}