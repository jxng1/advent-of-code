package main.java.com.jxng1.days;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day19 extends Day {

    public Day19(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        return String.valueOf(solve(parseScanners(input)).getValue().size());
    }

    @Override
    String task2(List<String> input) {
        var solved = solve(parseScanners(input)).getKey();

        return String.valueOf(solved
                .stream()
                .flatMapToInt(a -> solved.stream().mapToInt(b -> Point3D.calculateManhattanDistance(a, b)))
                .max()
                .getAsInt());
    }

    private LinkedList<Scanner> parseScanners(List<String> input) {
        LinkedList<Scanner> tmp = new LinkedList<>();

        int startIndex = 0;
        int endIndex = 0;
        int scannerID = 0;
        for (String s : input) {
            if (s.contains("--- scanner")) {
                startIndex = endIndex + 1;
                scannerID = Integer.parseInt(s.substring(12, s.indexOf(" ", 12)));
            } else if (s.isBlank() || endIndex == input.size() - 1) {
                tmp.add(new Scanner(scannerID, input.subList(startIndex, endIndex + 1).stream().filter(point -> !point.isBlank()).collect(Collectors.toCollection(LinkedList::new))));
            }
            endIndex++;
        }

        return tmp;
    }

    private AbstractMap.SimpleEntry<Set<Point3D>, Set<Point3D>> solve(LinkedList<Scanner> scanners) {
        var base = scanners.remove();
        base.setLocation(new Point3D(0, 0, 0));
        var unsolvedScanners = new LinkedList<>(scanners);
        LinkedHashSet<Scanner> solvedScanners = new LinkedHashSet<>();
        solvedScanners.add(base);

        while (!unsolvedScanners.isEmpty()) {
            var scanner = unsolvedScanners.remove();
            findOffsetIfIntersects(base, scanner).ifPresentOrElse(t -> {
                solvedScanners.add(scanner);
                scanner.setLocation(t.getKey());
                base.getBeacons().addAll(t.getValue());
            }, () -> unsolvedScanners.add(scanner));
        }

        return new AbstractMap.SimpleEntry<>(solvedScanners.stream().map(Scanner::getLocation).collect(Collectors.toCollection(LinkedHashSet::new)), base.getBeacons());
    }

    private Optional<AbstractMap.SimpleEntry<Point3D, Set<Point3D>>> findOffsetIfIntersects(Scanner a, Scanner b) {
        return IntStream.range(0, 6).boxed().flatMap(i -> {
            var aBeacons = a.getBeacons();
            return IntStream.range(0, 4).mapToObj(j -> {
                var bTransformedBeacons = b.orientateBeacons(i, j);

                for (var b1 : aBeacons) {
                    for (var b2 : bTransformedBeacons) {
                        var offset = b1.subtract(b2);
                        var mapped = bTransformedBeacons.stream().map(beacon -> beacon.add(offset)).collect(Collectors.toSet());
                        var moved = mapped.stream().filter(aBeacons::contains).collect(Collectors.toSet());

                        if (moved.size() >= 12) {
                            return new AbstractMap.SimpleEntry<>(offset, mapped);
                        }
                    }
                }

                return null;
            });
        }).filter(Objects::nonNull).findAny();
    }

    class Scanner {
        private int id;
        private Point3D location;
        private LinkedHashSet<Point3D> beacons;

        public Scanner(int id, List<String> beaconPositions) {
            this.id = id;
            this.beacons = parseBeacons(beaconPositions);
        }

        public LinkedHashSet<Point3D> getBeacons() {
            return beacons;
        }

        public Point3D getLocation() {
            return location;
        }

        public void setLocation(Point3D location) {
            this.location = location;
        }

        private LinkedHashSet<Point3D> parseBeacons(List<String> positions) {
            return positions.stream().map(s -> {
                var coords = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();

                return new Point3D(coords[0], coords[1], coords[2]);
            }).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        private LinkedHashSet<Point3D> orientateBeacons(int face, int rot) {
            return beacons.stream().map(b -> b.facing(face).rotate(rot)).collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    class Point3D {
        private int x;
        private int y;
        private int z;

        public Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public Point3D facing(int face) {
            switch (face) {
                case 0 -> {
                    return this;
                }
                case 1 -> {
                    return new Point3D(x, -y, -z);
                }
                case 2 -> {
                    return new Point3D(x, -z, y);
                }
                case 3 -> {
                    return new Point3D(-y, -z, x);
                }
                case 4 -> {
                    return new Point3D(y, -z, -x);
                }
                case 5 -> {
                    return new Point3D(-x, -z, -y);
                }
            }

            return null;
        }

        public Point3D rotate(int rot) {
            switch (rot) {
                case 0 -> {
                    return this;
                }
                case 1 -> {
                    return new Point3D(-y, x, z);
                }
                case 2 -> {
                    return new Point3D(-x, -y, z);
                }
                case 3 -> {
                    return new Point3D(y, -x, z);
                }
            }

            return null;
        }

        public static int calculateManhattanDistance(Point3D a, Point3D b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y) + Math.abs(a.z - b.z);
        }

        public Point3D add(Point3D other) {
            return new Point3D(this.x + other.x, this.y + other.y, this.z + other.z);
        }

        public Point3D subtract(Point3D other) {
            return new Point3D(this.x - other.x, this.y - other.y, this.z - other.z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point3D point3D = (Point3D) o;
            return x == point3D.x && y == point3D.y && z == point3D.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return "Point3D{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }
}