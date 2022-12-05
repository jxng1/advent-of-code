package main.java.com.jxng1.aoc22.days;

import main.java.com.jxng1.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 extends Day {

    public Day5(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        var crateStackList = parseInputToCrateStacks(input.subList(0, 8));
        var instructions = input.subList(10, input.size()).stream().map(this::parseInstruction).collect(Collectors.toList());

        instructions.forEach(instruction -> execute9000Instruction(instruction.count(), crateStackList.get(instruction.from()), crateStackList.get(instruction.to())));

        return crateStackList.stream().map(Stack::pop).map(String::valueOf).reduce("", (a, b) -> (a + b));
    }

    @Override
    protected String task2(List<String> input) {
        var crateStackList = parseInputToCrateStacks(input.subList(0, 8));
        var instructions = input.subList(10, input.size()).stream().map(this::parseInstruction).collect(Collectors.toList());

        instructions.forEach(instruction -> execute9001Instruction(instruction.count(), crateStackList.get(instruction.from()), crateStackList.get(instruction.to())));

        return crateStackList.stream().map(Stack::pop).map(String::valueOf).reduce("", (a, b) -> (a + b));
    }

    void execute9000Instruction(int moveCount, Stack<Character> fromStack, Stack<Character> toStack) {
        while (moveCount != 0 && !fromStack.isEmpty()) {
            toStack.push(fromStack.pop());
            moveCount--;
        }
    }

    void execute9001Instruction(int moveCount, Stack<Character> fromStack, Stack<Character> toStack) {
        Stack<Character> tmp = new Stack<>();

        while (moveCount != 0 && !fromStack.isEmpty()) {
            tmp.push(fromStack.pop());
            moveCount--;
        }

        while (!tmp.isEmpty()) {
            toStack.push(tmp.pop());
        }
    }

    List<Stack<Character>> parseInputToCrateStacks(List<String> input) {
        List<Stack<Character>> crateStackList = new ArrayList<>();

        for (int i = 0; i < input.size() + 1; ++i) {
            Stack<Character> stack = new Stack<>();
            crateStackList.add(stack);
        }

        IntStream
                .range(0, input.size())
                .forEach(i -> {
                    var tmp = parseLineToCrateRow(input.get(input.size() - i - 1));
                    for (int j = 0; j < tmp.length; ++j) {
                        if (!tmp[j].isBlank()) {
                            crateStackList.get(j).push(tmp[j].charAt(0));
                        }
                    }
                });

        return crateStackList;
    }

    String[] parseLineToCrateRow(String s) {
        return IntStream
                .range(0, s.length() / 4 + 1)
                .mapToObj(i -> s.substring(i * 4, (i + 1) * 4 - 1).replace("[", "").replace("]", ""))
                .toArray(String[]::new);
    }

    Instruction parseInstruction(String s) {
        var cleaned = Arrays.stream(s.replaceAll("[^ 0-9]", "").split(" ")).filter(c -> !c.isBlank()).mapToInt(Integer::parseInt).toArray();
        return new Instruction(cleaned[0], cleaned[1] - 1, cleaned[2] - 1);
    }
}

record Instruction(int count, int from, int to) {
}
