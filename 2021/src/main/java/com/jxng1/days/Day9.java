package main.java.com.jxng1.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Day9 extends Day {
    public Day9(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        Integer[][] board = makeIntegerGrid(input);

        int total = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (isLowPoint(board, row, col)) {
                    total += board[row][col] + 1;
                }
            }
        }

        return String.valueOf(total);
    }

    @Override
    String task2(List<String> input) {
        Integer[][] board = makeIntegerGrid(input);

        Boolean[][] visited = new Boolean[board.length][board[0].length];

        List<Integer> basinSizes = new LinkedList<>();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (isLowPoint(board, row, col)) {
                    for (var r : visited) {
                        Arrays.fill(r, false);
                    }

                    basinSizes.add((int) Arrays.stream(basin(board, visited, row, col))
                            .flatMap(Arrays::stream)
                            .filter(b -> b).count());
                }
            }
        }

        return String.valueOf(basinSizes.stream().sorted().skip(Math.max(0, basinSizes.size() - 3)).reduce((m, n) -> m * n).get());
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

    public boolean isLowPoint(Integer[][] board, int row, int col) {
        // 3 cases: corner(2 checks), edge(3 checks) and normal(4 checks);
        return checkBoard(board, board[row][col], row, col - 1) // left
                && checkBoard(board, board[row][col], row, col + 1) // right
                && checkBoard(board, board[row][col], row - 1, col) // up
                && checkBoard(board, board[row][col], row + 1, col); // down
    }

    public static boolean checkBoard(Integer[][] board, int val, int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
            return true;
        }

        return val < board[row][col];
    }

    public static Boolean[][] basin(Integer[][] board, Boolean[][] visited, int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
            return visited;
        } else if (board[row][col] == 9) {
            return visited;
        } else if (visited[row][col]) {
            return visited;
        }

        visited[row][col] = true;
        basin(board, visited, row, col - 1);
        basin(board, visited, row, col + 1);
        basin(board, visited, row - 1, col);
        basin(board, visited, row + 1, col);

        return visited;
    }
}
