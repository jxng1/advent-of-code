package main.java.com.jxng1.days;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Day10 extends Day {
    public Day10(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        int total = 0;
        for (String line : input) {
            switch (findFirstIncorrectClosingCharacter(line)) {
                case ')' -> total += 3;
                case ']' -> total += 57;
                case '}' -> total += 1197;
                case '>' -> total += 25137;
            }
        }

        return String.valueOf(total);
    }

    @Override
    String task2(List<String> input) {
        List<Long> scores = new LinkedList<>();
        for (String line : input) {
            if (findFirstIncorrectClosingCharacter(line) == ' ') { // if not corrupted
                var query = finishIncompleteLine(line);
                long total = 0;
                for (char c : query) {
                    total = accumulateScore(c, total);
                }

                scores.add(total);
            }
        }

        return String.valueOf(scores.stream().sorted().collect(Collectors.toList()).get(scores.size() / 2));
    }

    private char findFirstIncorrectClosingCharacter(String input) {
        Stack<Character> openingCharacterStack = new Stack<>();
        var split = input.toCharArray();
        int i = 0;

        // if char is opener, push to stack
        // if char is closer, pop stack and check if it is the other half of opener
        do {
            if (split[i] == '{' || split[i] == '[' || split[i] == '(' || split[i] == '<') {
                openingCharacterStack.push(split[i]);
            } else {
                if (!matches(split[i], openingCharacterStack.pop())) {
                    return split[i];
                }
            }
            i++;
        } while (i < split.length);

        return ' ';
    }

    private List<Character> finishIncompleteLine(String input) {
        Stack<Character> openingCharacterStack = new Stack<>();
        var split = input.toCharArray();
        List<Character> characters = new LinkedList<>();
        int i = 0;

        do {
            if (split[i] == '{' || split[i] == '[' || split[i] == '(' || split[i] == '<') {
                openingCharacterStack.push(split[i]);
            } else {
                openingCharacterStack.pop();
            }
            i++;
        } while (i < split.length);

        while (!openingCharacterStack.isEmpty()) {
            char popped = openingCharacterStack.pop();

            if (popped == '(') {
                characters.add(')');
            } else if (popped == '[') {
                characters.add(']');
            } else if (popped == '{') {
                characters.add('}');
            } else if (popped == '<') {
                characters.add('>');
            }
        }

        return characters;
    }

    private long accumulateScore(char c, long score) {
        switch (c) {
            case ')' -> {
                return score * 5 + 1;
            }
            case ']' -> {
                return score * 5 + 2;
            }
            case '}' -> {
                return score * 5 + 3;
            }
            case '>' -> {
                return score * 5 + 4;
            }
        }

        return score;
    }

    private boolean matches(char a, char b) {
        switch (a) {
            case ')' -> {
                return b == '(';
            }
            case ']' -> {
                return b == '[';
            }
            case '}' -> {
                return b == '{';
            }
            case '>' -> {
                return b == '<';
            }
        }

        return false;
    }
}
