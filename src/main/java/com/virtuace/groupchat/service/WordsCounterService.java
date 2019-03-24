package com.virtuace.groupchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WordsCounterService {

    private final ConcurrentMap<String, AtomicLong> wordCounters;

    @Autowired
    public WordsCounterService(ConcurrentMap<String, AtomicLong> wordCounters) {
        this.wordCounters = wordCounters;
    }

    public List<String> incrementCountersAndGetUpdated(Iterable<String> words) {
        List<String> updatedWords = new ArrayList<>();

        if (words == null) {
            return updatedWords;
        }

        words.forEach(word -> {
                word = word.toLowerCase();
                wordCounters.computeIfAbsent(word, w -> new AtomicLong()).incrementAndGet();
                updatedWords.add(word);
            }
        );

        return updatedWords;
    }
}
