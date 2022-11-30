package main.java.com.jxng1.aoc21.days;

import main.java.com.jxng1.Day;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day7 extends Day {
    public Day7(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        var sorted = Arrays
                .stream(input.toString().replaceAll("(\\[)|(\\])", "").split(","))
                .mapToInt(Integer::parseInt)
                .sorted()
                .boxed()
                .collect(Collectors.toList());
        int median = sorted.get(sorted.size() / 2);

        return String.valueOf(sorted.stream().map(num -> Math.abs(num - median)).collect(Collectors.summingInt(Integer::intValue)));
    }

    @Override
    protected String task2(List<String> input) {
        var transformed = Arrays
                .stream(input.toString().replaceAll("(\\[)|(\\])", "").split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList();
        int mean = transformed.stream().reduce(Integer::sum).get() / transformed.size();

        return String.valueOf(transformed.stream().map(num -> calculateTriangularCost(Math.abs(num - mean))).collect(Collectors.summingInt(Integer::intValue)));
    }

    public int calculateTriangularCost(int steps) {
        return steps * (steps + 1) / 2;
    }
}
