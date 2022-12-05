package main.java.com.jxng1.aoc22.days;

import main.java.com.jxng1.Day;

import java.util.Arrays;
import java.util.List;

public class Day4 extends Day {

    public Day4(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        return String.valueOf(input
                .stream()
                .mapToInt(s ->
                        {
                            var pairs = s.split(",");

                            return isContained(convertStringToIntArr(pairs[0].split("-")),
                                    convertStringToIntArr(pairs[1].split("-"))) ? 1 : 0;
                        }
                )
                .sum());
    }

    @Override
    protected String task2(List<String> input) {
        var tmp = String.valueOf(input
                .stream()
                .mapToInt(s ->
                        {
                            var pairs = s.split(",");

                            return isIntersecting(convertStringToIntArr(pairs[0].split("-")),
                                    convertStringToIntArr(pairs[1].split("-"))) ? 1 : 0;
                        }
                )
                .sum());

        return null;
    }

    boolean isContained(int[] a, int[] b) {
        return a[0] <= b[0] && a[1] >= b[1] || b[0] <= a[0] && b[1] >= a[1];
    }

    boolean isIntersecting(int[] a, int[] b) {
        return a[0] >= b[0] && a[0] <= b[1]
                || a[1] >= b[0] && a[1] <= b[1]
                || isContained(a, b);

    }

    int[] convertStringToIntArr(String[] arr) {
        return Arrays.stream(arr).mapToInt(Integer::parseInt).toArray();
    }
}
