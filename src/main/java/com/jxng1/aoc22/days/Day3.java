package main.java.com.jxng1.aoc22.days;

import main.java.com.jxng1.Day;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day3 extends Day {

    public Day3(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        return String.valueOf(input.stream()
                .map(this::splitCompartments)
                .map(s -> new String[]
                        {
                                removeDuplicateCharacters(s[0]),
                                removeDuplicateCharacters(s[1])
                        })
                .map(sS ->
                        {
                            Set<Character> tmp = new HashSet<>();
                            for (char c0 : sS[0].toCharArray()) {
                                for (char c1 : sS[1].toCharArray()) {
                                    if (c0 == c1) {
                                        tmp.add(c0);
                                    }
                                }
                            }

                            StringBuilder sb = new StringBuilder();
                            for (char c : tmp) {
                                sb.append(c);
                            }

                            return new String(sb);
                        }
                        // Using a for-loop was faster than regex replacement :eyes:
                        // {
                        //     return sS[0].replaceAll("[^" + sS[1] + "]", "");
                        // }
                )
                .flatMapToInt(s -> Arrays.stream(s.split("")).mapToInt(c -> calculatePriority(c.charAt(0))))
                .sum()
        );
    }

    @Override
    protected String task2(List<String> input) {
        return String.valueOf(IntStream.range(0, input.size() / 3)
                .mapToObj(i -> input.stream().skip(i * 3L).toArray(String[]::new))
                .map(sArr ->
                        {
                            Set<Character> common = new HashSet<>();

                            for (char c1 : sArr[0].toCharArray()) {
                                for (char c2 : sArr[1].toCharArray()) {
                                    for (char c3 : sArr[2].toCharArray()) {
                                        if (c1 == c2 && c2 == c3) {
                                            common.add(c1);
                                        }
                                    }
                                }
                            }

                            StringBuilder sb = new StringBuilder();
                            for (char c : common) {
                                sb.append(c);
                            }

                            return new String(sb);
                        }
                )
                .flatMapToInt(s -> Arrays.stream(s.split("")).mapToInt(c -> calculatePriority(c.charAt(0))))
                .sum());
    }

    String[] splitCompartments(String rucksackContents) {
        return new String[]
                {
                        rucksackContents.substring(0, rucksackContents.length() / 2),
                        rucksackContents.substring(rucksackContents.length() / 2)
                };
    }

    String removeDuplicateCharacters(String s) {
        return Arrays.stream(s.split("")).distinct().collect(Collectors.joining());
    }

    int calculatePriority(char c) {
        return c >= 97 ? c - 'a' + 1 : c - 'A' + 27;
    }
}
