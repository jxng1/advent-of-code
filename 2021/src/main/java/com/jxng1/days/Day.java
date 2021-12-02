package main.java.com.jxng1.days;

import main.java.com.jxng1.util.InputReader;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Day {

    public Day(String day) {
        List<Integer> input = InputReader.getInputReader().getInputAsList("day" + day + ".txt").stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        System.out.println("Day: " + day);
        System.out.println("Task 1 Output: " + task1(input));
        System.out.println("Task 2 Output: " + task1(input));
    }

    abstract int task1(List<Integer> input);

    abstract int task2(List<Integer> input);
}
