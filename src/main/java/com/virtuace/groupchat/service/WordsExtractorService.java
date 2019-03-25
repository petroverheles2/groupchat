package com.virtuace.groupchat.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WordsExtractorService {
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

        List<String> words = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sentence.length(); i++) {
            char ch = sentence.charAt(i);
            if (Character.isLetter(ch)) {
                stringBuilder.append(ch);
            } else if(stringBuilder.length() > 0) {
                words.add(stringBuilder.toString().toLowerCase());
                stringBuilder.setLength(0);
            }
        }

        // adding last word in sentence if there were no letter chars after it
        if (stringBuilder.length() > 0) {
            words.add(stringBuilder.toString().toLowerCase());
        }

        return words;
    }
}
