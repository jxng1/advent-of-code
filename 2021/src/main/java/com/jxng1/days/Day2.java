package main.java.com.jxng1.days;

import java.util.List;

public class Day2 extends Day {

    int distance;
    int depth;
    int aim;

    public Day2(int day) {
        super(day);
    }

    @Override
    int task1(List<String> input) {
        distance = 0;
        depth = 0;

        input.forEach(s -> {
            var split = s.split(" ");

            switch (split[0]) {
                case "forward" -> distance += Integer.parseInt(split[1]);
                case "up" -> depth -= Integer.parseInt(split[1]);
                case "down" -> depth += Integer.parseInt(split[1]);
            }
        });

        return distance * depth;
    }

    @Override
    int task2(List<String> input) {
        distance = 0;
        depth = 0;
        aim = 0;

        input.forEach(s -> {
            var split = s.split(" ");

            switch (split[0]) {
                case "forward" -> {
                    distance += Integer.parseInt(split[1]);
                    depth += aim * Integer.parseInt(split[1]);
                }
                case "up" -> aim -= Integer.parseInt(split[1]);
                case "down" -> aim += Integer.parseInt(split[1]);
            }
        });

        return distance * depth;
    }
}
