package main.java.com.jxng1.days;

import java.util.ArrayList;
import java.util.List;

public class Day3 extends Day {

    int index;

    public Day3(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        int inputLength = input.get(0).length();
        StringBuilder gammaRate = new StringBuilder();
        StringBuilder epsilonRate = new StringBuilder();

        for (int i = 0; i < inputLength; i++) { // each index
            int oneCount = 0;
            int zeroCount = 0;

            for (String s : input) { // each string
                if (s.charAt(i) == '1') {
                    oneCount++;
                } else {
                    zeroCount++;
                }
            }

            gammaRate.append(oneCount >= zeroCount ? "1" : "0");
            epsilonRate.append(zeroCount <= oneCount ? "0" : "1");
        }

        return String.valueOf(Integer.parseInt(String.valueOf(gammaRate), 2) * Integer.parseInt(String.valueOf(epsilonRate), 2));
    }

    @Override
    String task2(List<String> input) {
        index = 0;
        int inputLength = input.get(0).length();

        var oxyCopy = new ArrayList<>(input);
        var CO2Copy = new ArrayList<>(input);

        while (index < inputLength) {
            if (oxyCopy.size() != 1) {
                int oneCount = 0;
                int zeroCount = 0;

                for (String s : oxyCopy) {
                    if (s.charAt(index) == '1') {
                        oneCount++;
                    } else {
                        zeroCount++;
                    }
                }

                char mostCommon = oneCount >= zeroCount ? '1' : '0';

                oxyCopy.removeIf(s -> s.charAt(index) != mostCommon);
            }

            if (CO2Copy.size() != 1) {
                int oneCount = 0;
                int zeroCount = 0;

                for (String s : CO2Copy) {
                    if (s.charAt(index) == '1') {
                        oneCount++;
                    } else {
                        zeroCount++;
                    }
                }

                char leastCommon = zeroCount <= oneCount ? '0' : '1';

                CO2Copy.removeIf(s -> s.charAt(index) != leastCommon);
            }
            index++;
        }

        return String.valueOf(Integer.parseInt(oxyCopy.get(0), 2) * Integer.parseInt(CO2Copy.get(0), 2));
    }
}
