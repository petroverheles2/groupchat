package com.virtuace.groupchat.service;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class WordsCounterServiceTest {

    private WordsCounterService wordsCounterService;
    private ConcurrentSkipListMap<String, AtomicLong> wordCounters;
    private ConcurrentSkipListMap<String, Long> wordCountersUpdates;

    @Before
    public void setUp() {
        wordCounters = new ConcurrentSkipListMap<>();
        wordCountersUpdates = new ConcurrentSkipListMap<>();
        wordsCounterService = new WordsCounterService(wordCounters, wordCountersUpdates);
    }

    @Test
    public void nullArgument() {
        wordsCounterService.incrementCounters(null);
        assertThat(wordCounters).isEmpty();
        assertThat(wordCountersUpdates).isEmpty();
    }

    @Test
    public void emptyListArgument() {
        wordsCounterService.incrementCounters(new ArrayList<>(0));
        assertThat(wordCounters).isEmpty();
        assertThat(wordCountersUpdates).isEmpty();
    }

    @Test
    public void oneElementListArgument() {
        wordsCounterService.incrementCounters(Collections.singletonList("test"));

        assertThat(wordCounters).hasSize(1);
        assertThat(wordCounters.get("test").get()).isEqualTo(1L);

        assertThat(wordCountersUpdates).hasSize(1);
        assertThat(wordCountersUpdates.get("test")).isEqualTo(1L);
    }

    @Test
    public void twoElementsListArgument() {
        wordsCounterService.incrementCounters(Arrays.asList("test1", "test2"));

        assertThat(wordCounters).hasSize(2);
        assertThat(wordCounters.get("test1").get()).isEqualTo(1L);
        assertThat(wordCounters.get("test2").get()).isEqualTo(1L);

        assertThat(wordCountersUpdates).hasSize(2);
        assertThat(wordCountersUpdates.get("test1")).isEqualTo(1L);
        assertThat(wordCountersUpdates.get("test2")).isEqualTo(1L);    }

    @Test
    public void twoSameElementsListArgument() {
        wordsCounterService.incrementCounters(Arrays.asList("test3", "test3"));

        assertThat(wordCounters).hasSize(1);
        assertThat(wordCounters.get("test3").get()).isEqualTo(2L);

        assertThat(wordCountersUpdates).hasSize(1);
        assertThat(wordCountersUpdates.get("test3")).isEqualTo(2L);
    }

    @Test
    public void updateCounterAfterTwoCalls() {
        wordsCounterService.incrementCounters(Arrays.asList("test1", "test2"));

        assertThat(wordCounters).hasSize(2);
        assertThat(wordCounters.get("test1").get()).isEqualTo(1L);
        assertThat(wordCounters.get("test2").get()).isEqualTo(1L);

        assertThat(wordCountersUpdates).hasSize(2);
        assertThat(wordCountersUpdates.get("test1")).isEqualTo(1L);
        assertThat(wordCountersUpdates.get("test2")).isEqualTo(1L);

        wordCountersUpdates.clear();
        wordsCounterService.incrementCounters(Arrays.asList("test1", "test3"));

        assertThat(wordCounters).hasSize(3);
        assertThat(wordCounters.get("test1").get()).isEqualTo(2L);
        assertThat(wordCounters.get("test2").get()).isEqualTo(1L);
        assertThat(wordCounters.get("test3").get()).isEqualTo(1L);

        assertThat(wordCountersUpdates).hasSize(2);
        assertThat(wordCountersUpdates.get("test1")).isEqualTo(2L);
        assertThat(wordCountersUpdates.get("test3")).isEqualTo(1L);
    }

    @Test(timeout = 500L)
    public void performanceTestForSingleWord() {
        List<String> words = Collections.singletonList("test");
        IntStream.range(0, 1000000).forEach(i -> wordsCounterService.incrementCounters(words));
    }

    @Test(timeout = 500L)
    public void performanceParallelTestForSingleWord() {
        List<String> words = Collections.singletonList("test");
        IntStream.range(0, 1000000).parallel().forEach(i -> wordsCounterService.incrementCounters(words));
    }

    @Test(timeout = 500L)
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
                words.parallelStream().forEach(internalWords ->
                    wordsCounterService.incrementCounters(internalWords))
                );
    }
}