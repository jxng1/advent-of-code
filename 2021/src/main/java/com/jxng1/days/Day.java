package main.java.com.jxng1.days;

import main.java.com.jxng1.util.InputReader;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Day {

    public Day(int day) {
        List<String> input = InputReader.getInputReader().getInputAsList("day" + day + ".txt");

        System.out.println("Day: " + day);
        System.out.println("Task 1 Output: " + task1(input));
        System.out.println("Task 2 Output: " + task2(input));
        System.out.println("---");
    }

    abstract String task1(List<String> input);

    abstract String task2(List<String> input);

    public static List<Integer> convertToIntegerList(List<String> list) {
        return list.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
