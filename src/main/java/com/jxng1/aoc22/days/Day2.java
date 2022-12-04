package main.java.com.jxng1.aoc22.days;

import main.java.com.jxng1.Day;

import java.util.List;

public class Day2 extends Day {

    // 0 as rock, 1 as paper, 2 as scissors
    // index 0 is opp, index 1 is 'player'
    public static final int[][] PART_1_SCORE_TABLE =
            {//    ROCK  | PAPER | SCISSORS - Player scoring
                    {1 + 3, 2 + 6,     3},
                    {    1, 2 + 3, 3 + 6},
                    {1 + 6,     2, 3 + 3}
            };

    public static final int[][] PART_2_SCORE_TABLE =
            {//     X(LOSE) | PAPER(DRAW) | SCISSORS(WIN) - Player scoring
                    {3 + 0, 1 + 3, 2 + 6},
                    {1 + 0, 2 + 3, 3 + 6},
                    {2 + 0, 3 + 3, 1 + 6}
            };

    public Day2(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        return String.valueOf(input
                .stream()
                .mapToInt(s -> calculatePart1RoundOutcome(calculateWinMoveInd(s.charAt(0)), calculateWinMoveInd(s.charAt(2))))
                .sum());
    }

    @Override
    protected String task2(List<String> input) {
        return String.valueOf(input
                .stream()
                .mapToInt(s -> calculatePart2RoundOutcome(calculateWinMoveInd(s.charAt(0)), calculateWinMoveInd(s.charAt(2))))
                .sum());
    }

    int calculateWinMoveInd(char c) {
        return c == 'A' || c == 'X' ? 0
                : c == 'B' || c == 'Y' ? 1
                : c == 'C' || c == 'Z' ? 2
                : -1; // invalid
    }

    int calculatePart1RoundOutcome(int opMoveInd, int pMoveInd) {
        return PART_1_SCORE_TABLE[opMoveInd][pMoveInd];
    }

    int calculatePart2RoundOutcome(int opMoveInd, int pMoveInd) {
        return PART_2_SCORE_TABLE[opMoveInd][pMoveInd];
    }
}