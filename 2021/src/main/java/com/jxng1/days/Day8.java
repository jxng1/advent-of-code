package main.java.com.jxng1.days;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day8 extends Day {
    public Day8(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        List<Integer> check = Arrays.stream(new int[]{2, 4, 3, 7}).boxed().collect(Collectors.toList());

        var query = input.stream().map(s -> s.split("\\|")[1].trim()).collect(Collectors.toList());
        int total = 0;
        for (String output : query) {
            total += Arrays.stream(output.split(" ")).mapToInt(String::length).filter(check::contains).count();
        }

        return String.valueOf(total);
    }

    @Override
    String task2(List<String> input) {
        int total = 0;

        for (String s : input) {
            var split = Arrays.stream(s.split("\\|")).map(String::trim).toArray(String[]::new);
            total += solve(split[0], split[1]);
        }

        return String.valueOf(total);
    }

    private static int solve(String entry, String output) {
        String[] segments = new String[10];
        String ret = "";

        var split = Arrays.stream(entry.split(" "))
                .map(s -> Arrays.stream(s.split("")).sorted().collect(Collectors.joining()))
                .collect(Collectors.toList());

        segments[8] = split.stream().filter(s -> s.length() == 7).findFirst().get();
        segments[1] = split.stream().filter(s -> s.length() == 2).findFirst().get();
        segments[4] = split.stream().filter(s -> s.length() == 4).findFirst().get();
        segments[7] = split.stream().filter(s -> s.length() == 3).findFirst().get();
        List<String> zeroSixNine = split.stream().filter(s -> s.length() == 6).collect(Collectors.toList());
        List<String> twoThreeFive = split.stream().filter(s -> s.length() == 5).collect(Collectors.toList());
        segments[9] = zeroSixNine.stream().filter(s -> convertToCharList(s).containsAll(convertToCharList(segments[4]))).findFirst().get();
        zeroSixNine.remove(segments[9]);
        segments[0] = zeroSixNine.stream().filter(s -> convertToCharList(s).containsAll(convertToCharList(segments[7]))).findFirst().get();
        zeroSixNine.remove(segments[0]);
        segments[6] = zeroSixNine.get(0);
        segments[3] = twoThreeFive.stream().filter(s -> convertToCharList(s).containsAll(convertToCharList(segments[7]))).findFirst().get();
        twoThreeFive.remove(segments[3]);
        segments[5] = twoThreeFive.stream().filter(s -> convertToCharList(segments[6]).containsAll(convertToCharList(s))).findFirst().get();
        twoThreeFive.remove(segments[5]);
        segments[2] = twoThreeFive.get(0);

        for (String s : Arrays.stream(output.split(" "))
                .map(s -> Arrays.stream(s.split("")).sorted().collect(Collectors.joining()))
                .collect(Collectors.toList())) {
            ret += String.valueOf(Arrays.stream(segments).toList().indexOf(Arrays.stream(segments).filter(seg -> seg.equals(s)).findFirst().get()));
        }

        return Integer.parseInt(ret);
    }

    private static List<Character> convertToCharList(String s) {
        return s.chars().mapToObj(i -> (char) i).collect(Collectors.toList());
    }
}
