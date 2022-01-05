package main.java.com.jxng1.days;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day5 extends Day {
    public Day5(int day) {
        super(day);
    }

    @Override
    int task1(List<String> input) {
        var query = part1(input);

        return (int) IntStream.range(0, query.length)
                .flatMap(r -> Arrays.stream(query[r]))
                .filter(i -> i >= 2)
                .count();
    }

    @Override
    int task2(List<String> input) {
        var query = part2(input);

        return (int) IntStream.range(0, query.length)
                .flatMap(r -> Arrays.stream(query[r]))
                .filter(i -> i >= 2)
                .count();
    }

    private int[][] part1(List<String> input) {
        int[][] out = new int[1000][1000];

        for (String s : input) {
            var split = s.replace(" ", "").split("->");
            var x1y1 = split[0].split(",");
            var x2y2 = split[1].split(",");

            int x1 = Integer.parseInt(x1y1[0]);
            int y1 = Integer.parseInt(x1y1[1]);
            int x2 = Integer.parseInt(x2y2[0]);
            int y2 = Integer.parseInt(x2y2[1]);

            if (x1 == x2) {
                for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                    out[x1][i]++;
                }
            } else if (y1 == y2) {
                for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                    out[i][y1]++;
                }
            }
        }

        return out;
    }

    private int[][] part2(List<String> input) {
        int[][] out = new int[1000][1000];

        for (String s : input) {
            var split = s.replace(" ", "").split("->");
            var x1y1 = split[0].split(",");
            var x2y2 = split[1].split(",");

            int x1 = Integer.parseInt(x1y1[0]);
            int y1 = Integer.parseInt(x1y1[1]);
            int x2 = Integer.parseInt(x2y2[0]);
            int y2 = Integer.parseInt(x2y2[1]);

            // if diagonal, diff is the same between x1 x2 and y1 y2
            if (Math.abs(x2 - x1) == Math.abs(y2 - y1)) {
                int m = (x2 - x1) / (y2 - y1); // slope
                if (m == 1) { // positive slope aka bl -> tr
                    int cnt = 0;
                    int y;

                    int xMin = Math.min(x1, x2);
                    if (xMin == x1) {
                        y = y1;
                    } else {
                        y = y2;
                    }

                    for (int i = xMin; i <= Math.max(x1, x2); i++) {
                        out[i][y + cnt++]++;
                    }
                } else if (m == -1) { // negative slope aka tl -> br
                    int cnt = 0;
                    int y;

                    int xMax = Math.max(x1, x2);
                    if (xMax == x1) {
                        y = y1;
                    } else {
                        y = y2;
                    }

                    for (int i = xMax; i >= Math.min(x1, x2); i--) {
                        out[i][y + cnt++]++;
                    }
                }
            } else if (x1 == x2) { // horizontal
                for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                    out[x1][i]++;
                }
            } else if (y1 == y2) { // vertical
                for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                    out[i][y1]++;
                }
            }
        }

        return out;
    }
}
