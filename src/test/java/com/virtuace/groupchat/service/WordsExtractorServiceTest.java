package com.virtuace.groupchat.service;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.stream.IntStream;

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
    public void wordWithNumbers() {
        Iterable<String> words = wordsExtractorService.extract("this is 4u");
        assertThat(words).hasSize(3);
        assertThat(words).contains("this");
        assertThat(words).contains("is");
        assertThat(words).contains("4u");
    }

    @Test
    public void wordWithApostroph() {
        Iterable<String> words = wordsExtractorService.extract("it's my life");
        assertThat(words).hasSize(4);
        assertThat(words).contains("it");
        assertThat(words).contains("s");
        assertThat(words).contains("my");
        assertThat(words).contains("life");
    }

    @Test
    public void wordWithDash() {
        Iterable<String> words = wordsExtractorService.extract("Hi-tech news");
        assertThat(words).hasSize(2);
        assertThat(words).contains("hi-tech");
        assertThat(words).contains("news");
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

    @Test(timeout = 500L)
    public void permormanceTest() {
        IntStream.range(0, 100000).forEach(i -> wordsExtractorService.extract("Привет всем. Приятно познакомиться!"));
    }

}