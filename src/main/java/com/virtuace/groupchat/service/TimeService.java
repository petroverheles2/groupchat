package com.virtuace.groupchat.service;

import org.springframework.stereotype.Service;

@Service
public class TimeService {
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
