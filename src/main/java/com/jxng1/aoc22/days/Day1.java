package main.java.com.jxng1.aoc22.days;

import main.java.com.jxng1.Day;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day1 extends Day {
    public Day1(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        return String.valueOf(execute(input).max(java.util.Comparator.naturalOrder()).orElse(0));
    }

    @Override
    protected String task2(List<String> input) {
        return String.valueOf(execute(input)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList()).subList(0, 3)
                .stream().reduce(0, Integer::sum));
    }

    Stream<Integer> execute(List<String> input) {
        return Arrays.stream(normaliseInput(input).split(","))
                .map(line -> Arrays.stream(line.split(" ")).filter(s -> !s.isBlank()).mapToInt(Integer::parseInt).sum());
    }

    String normaliseInput(List<String> input) {
        return input.stream().reduce("", (a, b) -> b.isBlank() ? a + "," + b : a + " " + b);
    }
}
