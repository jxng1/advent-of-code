package main.java.com.jxng1.aoc21.days;

import main.java.com.jxng1.Day;

import java.util.List;
import java.util.stream.IntStream;

public class Day1 extends Day {

    public Day1(int day) {
        super(day);
    }

    protected String task1(List<String> input) {
        List<Integer> intInput = convertToIntegerList(input);

        return String.valueOf(IntStream.range(0, input.size() - 1)
                .filter(i -> intInput.get(i) < intInput.get(i + 1))
                .mapToObj(input::get).count());
    }

    protected String task2(List<String> input) {
        List<Integer> intInput = convertToIntegerList(input);

        return String.valueOf(IntStream.range(0, input.size() - 3)
                .filter(i -> find3Sum(intInput, i) < find3Sum(intInput, i + 1))
                .mapToObj(input::get).count());
    }

    private static int find3Sum(List<Integer> input, int index) {
        return IntStream.range(index, index + 3).map(input::get).sum();
    }
}


