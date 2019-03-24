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

    /**
     *
     * Extracts all the words in the given string, stripping numbers, punctuation and special chars
     * The extracted words are transformed to lower case
     *
     * @param sentence incoming sentence
     * @return the extracted list of words
     */
    public Iterable<String> extract(String sentence) {
        if (sentence == null) {
            return Collections.emptyList();
        }

        Matcher matcher = PATTERN.matcher(sentence);

        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group().toLowerCase());
        }

        return words;
    }
}
