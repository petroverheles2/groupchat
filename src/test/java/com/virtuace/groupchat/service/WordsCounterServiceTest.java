package com.virtuace.groupchat.service;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class WordsCounterServiceTest {

    private WordsCounterService wordsCounterService;
    private ConcurrentHashMap<String, AtomicLong> wordCounters;

    @Before
    public void setUp() {
        wordCounters = new ConcurrentHashMap<>();
        wordsCounterService = new WordsCounterService(wordCounters);
    }

    @Test
    public void nullArgument() {
        List<String> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(null);
        assertThat(countersUpdate).isEmpty();
    }

    @Test
    public void emptyListArgument() {
        List<String> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(new ArrayList<>(0));
        assertThat(countersUpdate).isEmpty();
    }

    @Test
    public void oneElementListArgument() {
        List<String> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(Collections.singletonList("test"));
        assertThat(countersUpdate).hasSize(1);
        assertThat(countersUpdate).contains("test");
    }

    @Test
    public void twoElementsListArgument() {
        List<String> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("test1", "test2"));
        assertThat(countersUpdate).hasSize(2);
        assertThat(countersUpdate).contains("test1");
        assertThat(countersUpdate).contains("test2");
    }

    @Test
    public void twoSameElementsListArgument() {
        List<String> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("test3", "test3"));

        assertThat(countersUpdate).hasSize(2);
        assertThat(countersUpdate).contains("test3");
    }

    @Test
    public void keysTransformedToLowerCaseElementsListArgument() {
        List<String> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("Test1", "test2"));
        assertThat(countersUpdate).hasSize(2);
        assertThat(countersUpdate).contains("test1");
        assertThat(countersUpdate).contains("test2");
    }

    @Test
    public void updateCounterAfterTwoCalls() {
        wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("test1", "test2"));
        wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("test1", "test3"));

        assertThat(wordCounters).hasSize(3);
        assertThat(wordCounters.get("test1").get()).isEqualTo(2L);
        assertThat(wordCounters.get("test2").get()).isEqualTo(1L);
        assertThat(wordCounters.get("test3").get()).isEqualTo(1L);
    }

    @Test(timeout = 500L)
    public void performanceTestForSingleWord() {
        List<String> words = Collections.singletonList("test");
        IntStream.range(0, 1000000).forEach(i -> wordsCounterService.incrementCountersAndGetUpdated(words));
    }

    @Test(timeout = 500L)
    public void performanceParallelTestForSingleWord() {
        List<String> words = Collections.singletonList("test");
        IntStream.range(0, 1000000).parallel().forEach(i -> wordsCounterService.incrementCountersAndGetUpdated(words));
    }

    @Test
    public void performanceTestForLotsOfWords() {
        List<List<String>> words = new ArrayList<>();

        IntStream.rangeClosed(1, 10000).forEach(i -> {
            if (i % 10 == 0) {
                List<String> internalWords = new ArrayList<>();
                IntStream.range(i - 10, i).forEach(k -> internalWords.add("Usual-word-" + k));
                words.add(internalWords);
            }
        });

        IntStream.range(0, 100).forEach(i ->
                words.forEach(internalWords ->
                    wordsCounterService.incrementCountersAndGetUpdated(internalWords))
                );
    }
}