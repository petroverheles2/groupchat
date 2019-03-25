package com.virtuace.groupchat.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WordsCounterUpdateServiceTest {
    private WordsCounterUpdateService wordsCounterUpdateService;

    @Mock
    private SimpMessageSendingOperations simpMessageSendingOperations;

    private Set<String> wordUpdates = new HashSet<>();

    private Map<String, AtomicLong> wordCounters = new HashMap<>();

    @Before
    public void setUp() {
        wordsCounterUpdateService = new WordsCounterUpdateService(simpMessageSendingOperations, wordUpdates, wordCounters);
    }

    @Test
    public void sendUpdate() {
        wordUpdates.add("test");
        wordCounters.put("test", new AtomicLong(1L));
        wordsCounterUpdateService.sendUpdate();
        //Mockito.verify(simpMessageSendingOperations).convertAndSend("/topic/counters-update", wordUpdates);
        assertThat(wordUpdates).isEmpty();
    }

    @Test
    public void addWords() {
        wordUpdates.add("test");
        List<String> words = Arrays.asList("test1", "test2");
        wordsCounterUpdateService.addUpdatedWord(words);
        assertThat(wordUpdates).contains("test1");
        assertThat(wordUpdates).contains("test2");
    }
}