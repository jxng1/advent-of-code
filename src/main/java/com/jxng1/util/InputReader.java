package main.java.com.jxng1.util;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class InputReader {

    private static InputReader inputReader;

    private InputReader() {

    }

    public static InputReader getInputReader() {
        if (inputReader == null) {
            inputReader = new InputReader();
        }

        return inputReader;
    }

    public List<String> getInputAsList(String path) {
        try {
            URI uri = Objects.requireNonNull(this.getClass().getResource("/main/resources/" + path),
                    ConsoleColor.GREEN + "The fairies have spotted that a fatal error occurred here whilst trying to read input from the resource files!" + ConsoleColor.RED).toURI();

            return Files.readAllLines(Paths.get(uri), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
