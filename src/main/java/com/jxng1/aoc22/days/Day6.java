package main.java.com.jxng1.aoc22.days;

import main.java.com.jxng1.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day6 extends Day {
    public Day6(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        return String.valueOf(IntStream
                .range(0, input.get(0).length() - 4)
                .filter(i -> Arrays.stream(input.get(0).substring(i, i + 4).split("")).distinct().count() == 4)
                .findFirst().orElse(-4) + 4);
    }

    @Override
    protected String task2(List<String> input) {
        return String.valueOf(IntStream
                .range(0, input.get(0).length() - 14)
                .filter(i -> Arrays.stream(input.get(0).substring(i, i + 14).split("")).distinct().count() == 14)
                .findFirst().orElse(-14) + 14);
    }
}
