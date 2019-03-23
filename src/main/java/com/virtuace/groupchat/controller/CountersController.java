package com.virtuace.groupchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class CountersController {

    private final Map<String, AtomicLong> wordCounters;

    @Autowired
    public CountersController(Map<String, AtomicLong> wordCounters) {
        this.wordCounters = wordCounters;
    }

    @GetMapping("/current-counters")
    public Map<String, AtomicLong> getCurrentCounters() {
        return wordCounters;
    }
 }
