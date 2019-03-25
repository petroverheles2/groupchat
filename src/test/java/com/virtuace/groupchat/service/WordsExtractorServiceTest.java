package com.virtuace.groupchat.service;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WordsExtractorServiceTest {

    private static WordsExtractorService wordsExtractorService;

    @BeforeClass
    public static void setUp() {
        wordsExtractorService = new WordsExtractorService();
    }

    @Test
    public void nullString() {
        Iterable<String> words = wordsExtractorService.extract(null);
        assertThat(words).isEmpty();
    }

    @Test
    public void emptyString() {
        Iterable<String> words = wordsExtractorService.extract("");
        assertThat(words).isEmpty();
    }

    @Test
    public void spaceString() {
        Iterable<String> words = wordsExtractorService.extract("     ");
        assertThat(words).isEmpty();
    }

    @Test
    public void oneWord() {
        Iterable<String> words = wordsExtractorService.extract("word");
        assertThat(words).hasSize(1);
        assertThat(words).contains("word");
    }

    @Test
    public void oneWordOnlyLetters() {
        Iterable<String> words = wordsExtractorService.extract("hello!");
        assertThat(words).hasSize(1);
        assertThat(words).contains("hello");
    }

    @Test
    public void severalWords() {
        Iterable<String> words = wordsExtractorService.extract("Hello, everybody. Nice to meet you!");
        assertThat(words).hasSize(6);
        assertThat(words).contains("hello");
        assertThat(words).contains("everybody");
        assertThat(words).contains("nice");
        assertThat(words).contains("to");
        assertThat(words).contains("meet");
        assertThat(words).contains("you");
    }

    @Test
    public void cyrillicWord() {
        Iterable<String> words = wordsExtractorService.extract("Привет всем. Приятно познакомиться!");
        assertThat(words).hasSize(4);
        assertThat(words).contains("привет");
        assertThat(words).contains("всем");
        assertThat(words).contains("приятно");
        assertThat(words).contains("познакомиться");
    }

    @Test
    public void duplicatedWords() {
        Iterable<String> words = wordsExtractorService.extract("Привет привет");
        assertThat(words).hasSize(2);
        assertThat(words.iterator().next()).isEqualTo("привет");
        assertThat(words.iterator().next()).isEqualTo("привет");
    }

    @Test(timeout = 100L)
    public void permormanceTest() {
        for (int i = 0; i < 100000; i++) {
            wordsExtractorService.extract("Привет всем. Приятно познакомиться!");
        }
    }

}