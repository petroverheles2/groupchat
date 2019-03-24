package com.virtuace.groupchat.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WordsExtractorService {
    private static final Pattern PATTERN = Pattern.compile("(?<!\\pL|\\pN)\\pL+(?!\\pL|\\pN)");

    public Iterable<String> extract(String s) {
        if (s == null) {
            return Collections.emptyList();
        }

        Matcher matcher = PATTERN.matcher(s);

        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }

        return words;
    }
}
