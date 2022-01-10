package main.java.com.jxng1.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day11 extends Day {
    public Day11(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        int totalFlashes = 0;
        var grid = makeIntegerGrid(input);

        for (int step = 0; step < 100; step++) {
            simulate(grid);

            // set all entries >= 10 to be 0 as they have flashed
            totalFlashes += calculateFlashesInGeneration(grid);
        }

        return String.valueOf(totalFlashes);
    }

    @Override
    String task2(List<String> input) {
        int step = 0;
        var grid = makeIntegerGrid(input);

        do {
            step++;
            simulate(grid);
        } while (calculateFlashesInGeneration(grid) != 100);

        return String.valueOf(step);
    }

    private int calculateFlashesInGeneration(Integer[][] grid) {
        int flashes = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] >= 10) {
                    flashes++;
                    grid[row][col] = 0;
                }
            }
        }

        return flashes;
    }

    private void simulate(Integer[][] grid) {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col]++;

                if (grid[row][col] == 10) { // since energy level greater than 9 simulate surrounding flashes
                    flashSurrounding(grid, row, col);
                }
            }
        }
    }

    private void flashSurrounding(Integer[][] grid, int row, int col) {
        flashTile(grid, row - 1, col - 1); // top left
        flashTile(grid, row - 1, col); // top
        flashTile(grid, row - 1, col + 1); // top right
        flashTile(grid, row, col - 1); // left
        flashTile(grid, row, col + 1); // right
        flashTile(grid, row + 1, col - 1); // bottom left
        flashTile(grid, row + 1, col); // bottom
        flashTile(grid, row + 1, col + 1); // bottom right
    }

    private void flashTile(Integer[][] grid, int row, int col) {
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[row].length) { // boundary
            return;
        }

        grid[row][col]++;
        if (grid[row][col] == 10) { // check if flashed
            flashSurrounding(grid, row, col);
        }
    }

    private Integer[][] makeIntegerGrid(List<String> input) {
        ArrayList<ArrayList<Integer>> grid = new ArrayList<>();

        for (String line : input) {
            var list = Arrays.stream(line.split(""))
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            grid.add((ArrayList<Integer>) list);
        }

        return grid.stream()
                .map(row -> row.toArray(Integer[]::new))
                .toArray(Integer[][]::new);
    }
}
