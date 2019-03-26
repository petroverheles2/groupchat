package com.virtuace.groupchat.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

import static java.util.stream.Collectors.toList;

@Service
public class WordsExtractorService {

    private static final String SPLIT_REGEXP = "[\\.,\\s!;?:\"]+";

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

        return Arrays.stream(sentence.split(SPLIT_REGEXP)).filter(s -> !s.isEmpty()).map(String::toLowerCase).collect(toList());
    }
}
