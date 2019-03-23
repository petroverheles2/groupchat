package com.virtuace.groupchat.service;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class WordsCounterServiceTest {

    private WordsCounterService wordsCounterService;

    @Before
    public void setUp() {
        wordsCounterService = new WordsCounterService(new ConcurrentHashMap<>());
    }

    @Test
    public void nullArgument() {
        Map<String, Long> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(null);
        assertThat(countersUpdate).isEmpty();
    }

    @Test
    public void emptyListArgument() {
        Map<String, Long> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(new ArrayList<>(0));
        assertThat(countersUpdate).isEmpty();
    }

    @Test
    public void oneElementListArgument() {
        Map<String, Long> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(Collections.singletonList("test"));
        assertThat(countersUpdate).hasSize(1);
        assertThat(countersUpdate.get("test")).isEqualTo(1L);
    }

    @Test
    public void twoElementsListArgument() {
        Map<String, Long> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("test1", "test2"));
        assertThat(countersUpdate).hasSize(2);
        assertThat(countersUpdate.get("test1")).isEqualTo(1L);
        assertThat(countersUpdate.get("test2")).isEqualTo(1L);
    }

    @Test
    public void updateCounterInSecondCallListArgument() {
        wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("test1", "test2"));
        Map<String, Long> countersUpdate = wordsCounterService.incrementCountersAndGetUpdated(Arrays.asList("test1", "test3"));

        assertThat(countersUpdate).hasSize(2);
        assertThat(countersUpdate.get("test1")).isEqualTo(2L);
        assertThat(countersUpdate.get("test3")).isEqualTo(1L);
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