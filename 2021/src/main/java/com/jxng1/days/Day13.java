package main.java.com.jxng1.days;

import main.java.com.jxng1.util.ConsoleColor;

import java.util.*;
import java.util.stream.Collectors;

public class Day13 extends Day {
    public Day13(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        var cleanedInput = input.stream()
                .filter(s -> s.matches("[\\d]+,[\\d]+"))
                .collect(Collectors.toList());
        var instructions = input.stream()
                .filter(s -> !s.matches("[\\d]+,[\\d]+") && !s.isBlank())
                .map(s -> s.replace("fold along ", ""))
                .collect(Collectors.toList());
        var coordinateMapping = generateCoordinateMapping(cleanedInput);

        var split = instructions.get(0).split("=");
        coordinateMapping = fold(Integer.parseInt(split[1]), split[0].equals("x"), coordinateMapping);

        int total = 0;
        for (var entry : coordinateMapping.entrySet()) {
            for (int x : entry.getValue()) {
                total++;
            }
        }

        return String.valueOf(total);
    }

    @Override
    String task2(List<String> input) {
        var cleanedInput = input.stream()
                .filter(s -> s.matches("[\\d]+,[\\d]+"))
                .collect(Collectors.toList());
        var instructions = input.stream()
                .filter(s -> !s.matches("[\\d]+,[\\d]+") && !s.isBlank())
                .map(s -> s.replace("fold along ", ""))
                .collect(Collectors.toList());
        var coordinateMapping = generateCoordinateMapping(cleanedInput);

        for (String line : instructions) {
            var split = line.split("=");
            coordinateMapping = fold(Integer.parseInt(split[1]), split[0].equals("x"), coordinateMapping);
        }

        int yMax = Collections.max(coordinateMapping.keySet()) + 1; // row(downward direction)
        int xMax = coordinateMapping.values().stream() // column(right direction)
                .mapToInt(Collections::max)
                .max()
                .orElse(-1) + 1;

        Character[][] grid = new Character[xMax][yMax];
        for (var row : grid) {
            Arrays.fill(row, '.');
        }

        for (var entry : coordinateMapping.entrySet()) {
            for (int x : entry.getValue()) {
                grid[x][entry.getKey()] = '#';
            }
        }

        Arrays.stream(grid).forEach(row -> {
            Arrays.stream(row).forEach(c -> {
                if (c.equals('#')) {
                    System.out.print(ConsoleColor.RED + c + " " + ConsoleColor.RESET);
                } else {
                    System.out.print(ConsoleColor.GREEN + c + " " + ConsoleColor.RESET);
                }
            });
            System.out.println();
        });
        return "VALUE ABOVE";
    }

    private Map<Integer, Set<Integer>> generateCoordinateMapping(List<String> input) {
        HashMap<Integer, Set<Integer>> coordinatesMap = new HashMap<>();

        for (String line : input) {
            var split = line.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);

            coordinatesMap.computeIfAbsent(x, set -> new HashSet<>());
            coordinatesMap.get(x).add(y);
        }

        return coordinatesMap;
    }

    private Map<Integer, Set<Integer>> fold(int coordinate, boolean foldX, Map<Integer, Set<Integer>> map) {
        Map<Integer, Set<Integer>> coordinatesDeepCopyMap = new HashMap<>(map);

        // copy values for each key
        for (Map.Entry<Integer, Set<Integer>> e : map.entrySet()) {
            coordinatesDeepCopyMap.put(e.getKey(), new HashSet<>(e.getValue()));
        }

        if (foldX) { // fold on x
            map.entrySet().stream()
                    .filter(e -> e.getKey() > coordinate) // transpose all x coordinates that are > than the fold line
                    .forEach(e -> {
                        coordinatesDeepCopyMap.remove(e.getKey()); // remove from mapping as they will be discarded

                        // new location will be derived from:
                        // fold - (position - fold)
                        int newKey = coordinate - (e.getKey() - coordinate);
                        coordinatesDeepCopyMap.computeIfAbsent(newKey, set -> new HashSet<>());
                        coordinatesDeepCopyMap.get(newKey).addAll(e.getValue()); // transpose values
                    });
        } else { // fold on y
            map.forEach((key, value) -> value.stream()
                    .filter(y -> y > coordinate) // transpose all y coordinates that are > than the fold line
                    .forEach(y -> {
                        coordinatesDeepCopyMap.get(key).remove(y); // remove from mapping as they will be discarded

                        // new location will be derived from:
                        // fold - (position - fold)
                        coordinatesDeepCopyMap.get(key).add(coordinate - (y - coordinate));
                    }));
        }

        return coordinatesDeepCopyMap;
    }
}