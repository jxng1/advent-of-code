package main.java.com.jxng1.days;

import main.java.com.jxng1.util.InputReader;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Day {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public Day(int day) {
        List<String> input = InputReader.getInputReader().getInputAsList("day" + day + ".txt");

        System.out.println(ANSI_YELLOW + "Day: " + ANSI_PURPLE + day + ANSI_RESET);
        long task1Start = System.currentTimeMillis();
        String task1Output = task1(input);
        long task1End = System.currentTimeMillis();
        System.out.println("Task 1 Output: " + ANSI_RED + task1Output + ANSI_RESET);
        System.out.println("Task 1 Time: " + ANSI_GREEN + (task1End - task1Start) + "ms" + ANSI_RESET);

        long task2Start = System.currentTimeMillis();
        String task2Output = task2(input);
        long task2End = System.currentTimeMillis();
        System.out.println("Task 2 Output: " + ANSI_RED + task2Output + ANSI_RESET);
        System.out.println("Task 2 Time: " + ANSI_GREEN + (task2End - task2Start) + "ms" + ANSI_RESET);
        System.out.println("---");
    }

    abstract String task1(List<String> input);

    abstract String task2(List<String> input);

    public static List<Integer> convertToIntegerList(List<String> list) {
        return list.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
