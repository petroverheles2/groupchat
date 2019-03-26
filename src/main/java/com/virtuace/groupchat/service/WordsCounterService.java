package com.virtuace.groupchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WordsCounterService {

    private final Map<String, AtomicLong> wordCounters;

    @Autowired
    public WordsCounterService(Map<String, AtomicLong> wordCounters) {
        this.wordCounters = wordCounters;
    }

    public void incrementCounters(Iterable<String> words) {
        if (words == null) {
            return;
        }

        words.forEach(word ->
            wordCounters.computeIfAbsent(word, w -> new AtomicLong()).incrementAndGet()
        );
    }
}
