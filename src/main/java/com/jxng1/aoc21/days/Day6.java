package main.java.com.jxng1.aoc21.days;

import main.java.com.jxng1.Day;

import java.util.Arrays;
import java.util.List;

public class Day6 extends Day {
    public Day6(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        var ret = Arrays
                .stream(input.toString().replaceAll("(\\[)|(\\])", "").split(","))
                .mapToInt(Integer::parseInt)
                .toArray();

        return String.valueOf(simulate(ret, 80));
    }

    @Override
    protected String task2(List<String> input) {
        var ret = Arrays
                .stream(input.toString().replaceAll("(\\[)|(\\])", "").split(","))
                .mapToInt(Integer::parseInt)
                .toArray();


        return String.valueOf(simulate(ret, 256));
    }

    private long simulate(int[] input, int days) {
        long[] generations = new long[9];
        Arrays.fill(generations, 0);

        for (int fish : input) {
            generations[fish]++;
        }

        // only need to keep track of the cycles of fish, not the fish themselves...
        for (int i = 1; i <= days; i++) {
            long oldGen = generations[0]; // ones about to spawn a new generation

            for (int j = 1; j < generations.length; j++) {
                generations[j - 1] = generations[j]; // we shuffle them forward
            }
            generations[6] += oldGen; // these are the ones that have spawned new generations
            generations[8] = oldGen; // these are the ones that have just spawned.
        }

        return Arrays.stream(generations).sum();
    }
}
