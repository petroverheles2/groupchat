package com.virtuace.groupchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    public Map<String, Long> incrementCountersAndGetUpdated(Iterable<String> words) {
        Map<String, Long> countersUpdate = new HashMap<>();

        if (words == null) {
            return countersUpdate;
        }

        words.forEach(word -> {
                    Long currentCount = wordCounters.computeIfAbsent(word, w -> new AtomicLong()).incrementAndGet();
                    countersUpdate.put(word, currentCount);
                }
        );

        return countersUpdate;
    }
}
