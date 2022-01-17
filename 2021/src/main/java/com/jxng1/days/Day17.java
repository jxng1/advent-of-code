package main.java.com.jxng1.days;

import java.util.Arrays;
import java.util.List;

public class Day17 extends Day {
    public Day17(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        String[] cleaned = input.get(0).substring(13).trim().split(",");
        String[] yBounds = cleaned[1].substring(3).replace("..", " ").split(" ");

        return String.valueOf(calculateMaxHeight(Integer.parseInt(yBounds[0])));
    }

    @Override
    String task2(List<String> input) {
        String[] cleaned = input.get(0).substring(13).trim().split(",");
        String[] xBounds = cleaned[0].substring(2).replace("..", " ").split(" ");
        String[] yBounds = cleaned[1].substring(3).replace("..", " ").split(" ");

        return String.valueOf(bruteForceLandings(Arrays.stream(xBounds).mapToInt(Integer::parseInt).toArray()
                , Arrays.stream(yBounds).mapToInt(Integer::parseInt).toArray()));
    }

    private int bruteForceLandings(int[] xBounds, int[] yBounds) {
        int sum = 0;

        for (int y = yBounds[0]; y < Math.abs(yBounds[0]); y++) {
            for (int x = 1; x <= xBounds[1]; x++) {
                sum += simulate(x, y, xBounds, yBounds) ? 1 : 0;
            }
        }
        return sum;
    }

    private boolean simulate(int initXVel, int initYVel, int[] xBounds, int[] yBounds) {
        int xPos = 0;
        int yPos = 0;

        while (yPos > yBounds[1]) {
            xPos += initXVel;
            yPos += initYVel;

            if (initXVel != 0) {
                initXVel = initXVel > 0 ? initXVel - 1 : initXVel + 1;
            }
            initYVel -= 1;
        }
        while (yPos >= yBounds[0]) {
            if (xPos >= xBounds[0] && xPos <= xBounds[1]) {
                return true;
            }
            xPos += initXVel;
            yPos += initYVel;

            if (initXVel != 0) {
                initXVel = initXVel > 0 ? initXVel - 1 : initXVel + 1;
            }
            initYVel -= 1;
        }

        return false;
    }

    private int calculateMaxHeight(int y) {
        // uses n(n + 1) / 2 to sum the values that the initial vel may produce
        // for example, if v_0 = 9, then height is 9 + 8 + 7 + 6 + 5 + 4 + 3 + 2 + 1 == 9(9 + 1)/2 = 45
        // once we hit y = 0, our vel becomes -v_0 - 1(because the step has occurred, decrementing v).
        // therefore, if -v - 1 = minimum y it can get, it means that v = -min_y - 1.
        int n = -y - 1;

        return (n * (n + 1)) / 2;
    }
}
