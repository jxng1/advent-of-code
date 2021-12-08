package main.java.com.jxng1.days;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day4 extends Day {

    public Day4(int day) {
        super(day);
    }

    @Override
    int task1(List<String> input) {
        return bingoSolver(input).entrySet().stream().findFirst().get().getValue();
    }

    @Override
    int task2(List<String> input) {
        return bingoSolver(input).values().stream().reduce((first, second) -> second).get();
    }

    private Map<String[][], Integer> bingoSolver(List<String> input) {
        input = input.stream().filter(line -> !line.isBlank()).collect(Collectors.toList());
        var bingoStrip = Arrays.stream(input.get(0).split(",")).toList();

        List<String[][]> boards = new ArrayList<>();
        Map<String[][], Integer> wonBoards = new LinkedHashMap<>();
        String[][] board = null;
        int row = 0;

        for (int i = 1; i < input.size(); i++) { // each line(row); skip bingoStrip line
            if (row == 5) {
                row = 0;
            }

            if (row == 0) { // new set of board
                board = new String[5][5];
                boards.add(board);
            }

            var splitItems = Arrays.stream(input.get(i).split(" ")).filter(s -> !s.isBlank()).toArray(); // each number of a board
            for (int col = 0; col < 5; col++) { // populate row and column
                board[row][col] = (String) splitItems[col];
            }

            row++;
        }

        for (String s : bingoStrip) {
            boards.forEach(b -> {
                for (int x = 0; x < b.length; x++) {
                    for (int y = 0; y < b.length; y++) {
                        if (Objects.equals(b[x][y], s)) {
                            b[x][y] = "-1";

                            if (!wonBoards.containsKey(b) && boardHasWon(b)) {
                                IntStream stream = IntStream.range(0, b.length).flatMap(r -> Arrays.stream(b[r]).mapToInt(item -> {
                                    int total = 0;
                                    if (!Objects.equals(item, "-1")) {
                                        return Integer.parseInt(item);
                                    }

                                    return 0;
                                }));

                                wonBoards.put(b, stream.sum() * Integer.parseInt(s));
                            }
                        }
                    }
                }
            });
        }

        return wonBoards;
    }

    private static boolean boardHasWon(String[][] board) {
        var rows = Arrays.stream(board).toList();

        return IntStream.range(0, rows.size() - 1)
                .anyMatch(col -> IntStream.range(0, rows.size() - 1)
                        .allMatch(row -> Objects.equals(board[row][col], "-1")))
                || rows.stream().anyMatch(row -> Arrays.stream(row).allMatch(s -> Objects.equals(s, "-1")));
    }
}