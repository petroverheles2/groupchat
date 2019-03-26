package com.virtuace.groupchat.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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

    private Map<String, AtomicLong> wordCounters = new HashMap<>();

    @Before
    public void setUp() {
        wordsCounterUpdateService = new WordsCounterUpdateService(simpMessageSendingOperations, wordCounters);
    }

    @Test
    public void sendUpdate() {
        wordCounters.put("test", new AtomicLong(1L));
        wordsCounterUpdateService.sendUpdate(Arrays.asList("test"));
        ArgumentCaptor captor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(simpMessageSendingOperations).convertAndSend(Mockito.eq("/topic/counters-update"), captor.capture());
        assertThat(((Map)captor.getValue()).get("test")).isEqualTo(1L);
    }
}