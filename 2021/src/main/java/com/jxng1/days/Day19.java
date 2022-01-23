package main.java.com.jxng1.days;

import main.java.com.jxng1.util.InputReader;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day19 extends Day {
    public Day19(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        List<Scanner> scanners = parseScanners(input);
        var tmp = solve(scanners);

       // var test = parseScanners(InputReader.getInputReader().getInputAsList("day19sample.txt"));
       // var tmp = solve(test);

        return null;
    }

    @Override
    String task2(List<String> input) {
        return null;
    }

    private Set<Point3D> solve(List<Scanner> scanners) {
        LinkedHashSet<Point3D> map = new LinkedHashSet<>();
        LinkedList<Scanner> locatedScanners = new LinkedList<>();

        Scanner base = scanners.get(0);
        mapScanner(base, 0, 0, 0, map);
        locatedScanners.add(base);

        while (locatedScanners.size() < scanners.size()) {
            for (Scanner unlocatedScanner : scanners) {
                if (locatedScanners.contains(unlocatedScanner)) {
                    continue;
                }

                var hehe = unlocatedScanner.getOrientations().collect(Collectors.toList());
                unlocatedScanner.getOrientations().map(scanner -> {
                    var tmp = scanner;
                    return scannerOverlapsWithMap(scanner, 12, map);
                }).flatMap(Optional::stream).findFirst().ifPresent(offset -> {
                    mapScanner(unlocatedScanner, offset.x, offset.y, offset.z, map);
                    locatedScanners.add(unlocatedScanner);
                });
            }
        }

        return map;
    }

    private void mapScanner(Scanner scanner, int xOffset, int yOffset, int zOffset, Set<Point3D> map) {
        scanner.setPosition(xOffset, yOffset, zOffset);
        map.addAll(scanner.getBeaconPoints().stream().map(point -> new Point3D(point.getX() + xOffset, point.getY() + yOffset, point.getZ() + zOffset))
                .collect(Collectors.toList()));
    }

    private Optional<Point3D> scannerOverlapsWithMap(Scanner scanner, int overlapLimit, Set<Point3D> map) {
        var mapping = map.stream().flatMap(mapPoint -> scanner.getBeaconPoints().stream().map(scannerPoint -> subtractPoint(mapPoint, scannerPoint)))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        var query = mapping.entrySet()
                .stream()
                .filter(e -> e.getValue() >= overlapLimit)
                .findFirst()
                .map(Map.Entry::getKey);


        return query;
    }

    private Point3D subtractPoint(Point3D a, Point3D b) {
        return new Point3D(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
//        return new Point3D(Math.abs(a.getX()) - Math.abs(b.getX()), Math.abs(a.getY()) - Math.abs(b.getY()), Math.abs(a.getZ()) - Math.abs(b.getZ()));
    }

    private List<Scanner> parseScanners(List<String> input) {
        LinkedList<Scanner> tmp = new LinkedList<>();

        int startIndex = 0;
        int endIndex = 0;
        int scannerID = 0;
        for (String s : input) {
            if (s.contains("--- scanner")) {
                startIndex = endIndex + 1;
                scannerID = Integer.parseInt(s.substring(12, s.indexOf(" ", 12)));
            } else if (s.isBlank() || endIndex == input.size() - 1) {
                tmp.add(new Scanner(scannerID, input.subList(startIndex, endIndex)));
            }
            endIndex++;
        }

        return tmp;
    }

    class Scanner {
        private int id;
        private Point3D position;
        private List<Point3D> beaconPositions = new LinkedList<>();

        public Scanner(int id, List<String> beaconStringPositions) {
            this.id = id;

            for (String s : beaconStringPositions) {
                var split = s.split(",");

                beaconPositions.add(new Point3D(Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2])));
            }
        }

        public void setPosition(int x, int y, int z) {
            this.position = new Point3D(x, y, z);
        }

        public List<Point3D> getBeaconPoints() {
            return beaconPositions;
        }

        public Stream<Scanner> getOrientations() {
            return IntStream
                    .range(0, 6)
                    .boxed()
                    .flatMap(rollIndex -> Stream.concat(Stream.of(this.roll()),
                            IntStream.range(0, 3).boxed().map(turnIndex -> rollIndex % 2 == 0 ? this.turn() : this.reverseTurn())));
        }

        public void addOffsetToPosition(int xOffset, int yOffset, int zOffset) {
            this.position = this.position == null ? new Point3D(xOffset, yOffset, zOffset) : position.add(xOffset, yOffset, zOffset);
        }

        private Scanner turn() {
            this.beaconPositions.forEach(Point3D::turn);

            return this;
        }

        private Scanner reverseTurn() {
            this.beaconPositions.forEach(Point3D::reverseTurn);

            return this;
        }

        private Scanner roll() {
            this.beaconPositions.forEach(Point3D::roll);

            return this;
        }
    }

    class Point3D {
        private int x;
        private int y;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        private int z;

        public Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point3D add(int x, int y, int z) {
            this.x += x;
            this.y += y;
            this.z += z;

            return this;
        }

        public void roll() {
            int tmp = y;

            y = z;
            z = -tmp;
        }

        public void turn() {
            int tmp = x;

            x = -y;
            y = tmp;
        }

        public void reverseTurn() {
            int tmp = x;

            x = y;
            y = -tmp;
        }
    }
}