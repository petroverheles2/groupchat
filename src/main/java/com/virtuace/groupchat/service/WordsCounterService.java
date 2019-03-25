package com.virtuace.groupchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WordsCounterService {

    private final ConcurrentMap<String, AtomicLong> wordCounters;
    private final ConcurrentMap<String, Long> wordCountersUpdates;

    @Autowired
    public WordsCounterService(ConcurrentMap<String, AtomicLong> wordCounters, ConcurrentMap<String, Long> wordCountersUpdates) {
        this.wordCounters = wordCounters;
        this.wordCountersUpdates = wordCountersUpdates;
    }

    public void incrementCounters(Iterable<String> words) {
        if (words == null) {
            return;
        }

        words.forEach(word -> {
            long currentCount = wordCounters.computeIfAbsent(word, w -> new AtomicLong()).incrementAndGet();
            wordCountersUpdates.put(word, currentCount);
        });
    }
}
