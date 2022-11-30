package main.java.com.jxng1;

import main.java.com.jxng1.util.ConsoleColor;
import main.java.com.jxng1.util.InputReader;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Day {

    public Day(int day) {
        List<String> input = InputReader.getInputReader().getInputAsList(Main.WORKING_YEAR + "/day"+ day + ".txt");
        if (input == null)
        {
            System.out.println(ConsoleColor.GREEN + "The resource the elves are looking for doesn't exist! Did you point them to the correct location?.." + ConsoleColor.WHITE);
        }

        System.out.println(ConsoleColor.GREEN + "Day: " + ConsoleColor.RED + day + ConsoleColor.YELLOW + " - Year: " + ConsoleColor.RESET);
        System.out.println("-------------------------");

        long task1Start = System.currentTimeMillis();
        String task1Output = task1(input);
        long task1End = System.currentTimeMillis();
        System.out.println("Task 1 Output: " + ConsoleColor.YELLOW + task1Output + ConsoleColor.RESET);
        System.out.println("Task 1 Time: " + ConsoleColor.GREEN + (task1End - task1Start) + "ms" + ConsoleColor.RESET);

        long task2Start = System.currentTimeMillis();
        String task2Output = task2(input);
        long task2End = System.currentTimeMillis();
        System.out.println("Task 2 Output: " + ConsoleColor.RED + task2Output + ConsoleColor.RESET);
        System.out.println("Task 2 Time: " + ConsoleColor.YELLOW + (task2End - task2Start) + "ms" + ConsoleColor.RESET);
        System.out.println("<========================================================>");
    }

    protected abstract String task1(List<String> input);

    protected abstract String task2(List<String> input);

    public static List<Integer> convertToIntegerList(List<String> list) {
        return list.stream().map(Integer::parseInt).collect(Collectors.toList());
    }
}
