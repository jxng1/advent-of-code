package main.java.com.jxng1.aoc21.days;

import main.java.com.jxng1.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day15 extends Day {
    public Day15(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        var grid = generateIntegerGrid(input);

        return String.valueOf(findRiskSum(grid));
    }

    @Override
    protected String task2(List<String> input) {
        var grid = generateIntegerGrid(input);
        var expandedGrid = expandGrid(grid);

        return String.valueOf(findRiskSum(expandedGrid));
    }

    private Integer[][] generateIntegerGrid(List<String> input) {
        ArrayList<ArrayList<Integer>> grid = new ArrayList<>();

        for (String line : input) {
            var tmp = Arrays.stream(line.split(""))
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());
            grid.add((ArrayList<Integer>) tmp);
        }

        return grid.stream()
                .map(row -> row.toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }

    private int findRiskSum(Integer[][] grid) {
        Integer[][] riskSumGrid = new Integer[grid.length][grid[0].length];

        IntStream.range(0, riskSumGrid.length).forEach(i -> Arrays.fill(riskSumGrid[i], 1000000));

        // start from end(bottom-right) and work to start(top-left)...
        riskSumGrid[riskSumGrid.length - 1][riskSumGrid[0].length - 1] = 0;
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int row = riskSumGrid.length - 1; row >= 0; row--) {
                for (int col = riskSumGrid[row].length - 1; col >= 0; col--) {
                    int min = Integer.MAX_VALUE;

                    if (row - 1 >= 0) { // check up
                        min = Math.min(min, grid[row - 1][col] + riskSumGrid[row - 1][col]);
                    }
                    if (row + 1 < riskSumGrid.length) { // check down
                        min = Math.min(min, grid[row + 1][col] + riskSumGrid[row + 1][col]);
                    }
                    if (col - 1 >= 0) { // check left
                        min = Math.min(min, grid[row][col - 1] + riskSumGrid[row][col - 1]);
                    }
                    if (col + 1 < riskSumGrid[row].length) { // check right
                        min = Math.min(min, grid[row][col + 1] + riskSumGrid[row][col + 1]);
                    }

                    // ensures we loop through grid again after change is made, as the path might update
                    int oldRisk = riskSumGrid[row][col];
                    riskSumGrid[row][col] = Math.min(riskSumGrid[row][col], min);
                    if (riskSumGrid[row][col] != oldRisk) {
                        changed = true;
                    }
                }
            }
        }

        return riskSumGrid[0][0];
    }

    private Integer[][] expandGrid(Integer[][] grid) {
        Integer[][] newGrid = new Integer[grid.length * 5][grid[0].length * 5];

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                int originalValue = grid[row][col];

                // translate original value to 5 times larger(in the 5x5 grid)...
                // for each value in the 5 x 5 grid, it is incremented by how much i and j is moved...
                // the maximum adjustment value for the 5 x 5 would be 8, as i would be 4 and j would be 4...
                // this means the total maximum new value could reach 17(9 + 8)...
                // as this wraps back to 0-9 we take away 9 if the value > 9...
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        int newValue = originalValue + i + j;

                        if (newValue > 9) {
                            newValue -= 9;
                        }
                        newGrid[row + i * grid.length][col + j * grid[row].length] = newValue;
                    }
                }
            }
        }

        return newGrid;
    }
}
