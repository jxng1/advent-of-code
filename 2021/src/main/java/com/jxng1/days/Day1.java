package main.java.com.jxng1.days;

import java.util.List;
import java.util.stream.IntStream;

public class Day1 extends Day {

    public Day1(String day) {
        super(day);
    }

    public int task1(List<Integer> input) {
        return (int) IntStream.range(0, input.size() - 1)
                .filter(i -> input.get(i) < input.get(i + 1))
                .mapToObj(input::get).count();
    }

    public int task2(List<Integer> input) {
        return (int) IntStream.range(0, input.size() - 3)
                .filter(i -> find3Sum(input, i) < find3Sum(input, i + 1))
                .mapToObj(input::get).count();
    }

    private static int find3Sum(List<Integer> input, int index) {
        return IntStream.range(index, index + 3).map(input::get).sum();
    }
}


