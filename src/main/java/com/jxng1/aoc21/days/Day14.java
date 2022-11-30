package main.java.com.jxng1.aoc21.days;

import main.java.com.jxng1.Day;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 extends Day {
    public Day14(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        var instructionsMapping = generateInstructionsMap(input.subList(2, input.size()));
        HashMap<String, Long> pairingMap = new HashMap<>();
        var template = input.get(0).toCharArray();

        for (int i = 0; i + 1 < template.length; i++) {
            String key = template[i] + "" + template[i + 1];

            pairingMap.merge(key, 1L, Long::sum);
        }


        for (int i = 0; i < 10; i++) {
            pairingMap = polymerise(pairingMap, instructionsMapping);
        }

        return String.valueOf(getMaxDifference(generateCharacterMapping(pairingMap)));
    }

    @Override
    protected String task2(List<String> input) {
        var instructionsMapping = generateInstructionsMap(input.subList(2, input.size()));
        HashMap<String, Long> pairingMap = new HashMap<>();
        var template = input.get(0).toCharArray();

        for (int i = 0; i + 1 < template.length; i++) {
            String key = template[i] + "" + template[i + 1];

            pairingMap.merge(key, 1L, Long::sum);
        }


        for (int i = 0; i < 40; i++) {
            pairingMap = polymerise(pairingMap, instructionsMapping);
        }

        return String.valueOf(getMaxDifference(generateCharacterMapping(pairingMap)));
    }

    private Long getMaxDifference(Map<String, Long> map) {
        // we need to half it and add 1 as the mapping contains double the count for each character
        return (map.values().stream().max(Long::compare).get()
                - map.values().stream().min(Long::compare).get()) / 2 + 1;
    }

    private Map<String, String> generateInstructionsMap(List<String> input) {
        HashMap<String, String> map = new HashMap<>();

        input.forEach(line -> {
            var split = line.split(" -> ");
            map.put(split[0], split[1]);
        });

        return map;
    }

    private Map<String, Long> generateCharacterMapping(Map<String, Long> map) {
        Map<String, Long> characterMap = new HashMap<>();

        map.forEach((pair, count) -> {
            characterMap.merge(pair.charAt(0) + "", count, Long::sum);
            characterMap.merge(pair.charAt(1) + "", count, Long::sum);
        });

        return characterMap;
    }

    private HashMap<String, Long> polymerise(Map<String, Long> countMapping, Map<String, String> instructionsMap) {
        HashMap<String, Long> tmp = new HashMap<>();

        // ABCDEFGH
        // AB BC CD DE EF FG GH
        // store the count of mappings
        countMapping.forEach((pair, count) -> {
            tmp.merge(pair.charAt(0) + instructionsMap.get(pair), count, Long::sum);
            tmp.merge(instructionsMap.get(pair) + pair.charAt(1), count, Long::sum);
        });

        return tmp;
    }
}