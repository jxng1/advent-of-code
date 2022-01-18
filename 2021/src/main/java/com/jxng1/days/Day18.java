package main.java.com.jxng1.days;

import java.util.List;

public class Day18 extends Day {
    public Day18(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        var test = parsePair(input.get(0));

        return null;
    }

    @Override
    String task2(List<String> input) {
        return null;
    }

    private Pair parsePair(String input) {
        int openBrackets = 0;
        int commaIndex = 0;

        pairCharacterLoop:
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            switch (c) {
                case '[' -> openBrackets++;
                case ']' -> openBrackets--;
                case ',' -> {
                    if (openBrackets == 1) {
                        commaIndex = i;
                        break pairCharacterLoop;
                    }
                }
            }
        }

        String left = input.substring(1, commaIndex); // start index of 1 means we skip the opening bracket
        String right = input.substring(commaIndex + 1, input.length() - 1); // commandIndex + 1 means we skip the comma, input.length() - 1 means we don't go out of bounds
// try to do this with input.length() - 2 instead and modify parseInput...


        return new Pair(parseInput(left), parseInput(right));
    }

    private Pair parseInput(String input) {
        if (input.charAt(0) != '[') { // input parsed is a regular number
            Pair pair = new Pair(null, null);
            pair.setValue(Integer.parseInt(input));

            return pair;
        } else { // input parsed is another pair
            int endOfPair = findPairEndingIndex(input);
            String pairString = input.substring(0, endOfPair + 1); // as substring end index is exclusive

            return parsePair(pairString);
        }
    }

    private int findPairEndingIndex(String input) {
        int openBrackets = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '[') {
                openBrackets++;
            } else if (input.charAt(i) == ']') {
                openBrackets--;
            }

            if (openBrackets == 0) {
                return i;
            }
        }

        return -1;
    }

    class Pair {
        Pair left;
        Pair right;
        int value;

        public Pair(Pair left, Pair right) {
            this.left = left;
            this.right = right;
        }

        public boolean isRegular() {
            return left == null && right == null;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
