package main.java.com.jxng1.days;

import main.java.com.jxng1.util.InputReader;

import java.util.*;
import java.util.stream.Collectors;

public class Day19 extends Day {
    public Day19(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        //List<Scanner> scanners = parseScanners(input);
        //var tmp = solve(scanners);

        var ret = parseScanners(InputReader.getInputReader().getInputAsList("day19sample.txt"));
        var tmp = solve(ret);
        var sorted = tmp.stream().sorted(Comparator.comparing(Point3D::getX)).collect(Collectors.toCollection(LinkedHashSet::new));

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

        for (Scanner a : scanners) {
            for (Scanner b : scanners) {
                if (a.getID() == b.getID() || a.getMap().containsKey(b) || b.getMap().containsKey(a)) {
                    continue;
                }
                if (a.getMap().get(b) != null && !a.getMap().get(b).isEmpty()) {
                    continue;
                }

                Set<Point3D> overlappingPoints = a.calculateOverlappingBeacons(b);
                if (!overlappingPoints.isEmpty()) {
                    a.mapScanner(b, overlappingPoints);
                    b.mapScanner(a, overlappingPoints);
                }
            }
        }

//        while (locatedScanners.size() < scanners.size()) {
//            for (Scanner unlocatedScanner : scanners) {
//                if (locatedScanners.contains(unlocatedScanner)) {
//                    continue;
//                }
//
//                var overlappingBeacons = scannerOverlapsMap(map, unlocatedScanner, 12);
//                if (overlappingBeacons != null) {
//                    System.out.println(unlocatedScanner.id);
//                    mapScanner(unlocatedScanner, overlappingBeacons, map);
//                    locatedScanners.add(unlocatedScanner);
//                }
//
////                scannerOverlapsMap(map, unlocatedScanner, 12).ifPresent(offset -> {
////                    mapScanner(unlocatedScanner, offset.getX(), offset.getY(), offset.getZ(), map);
////                    locatedScanners.add(unlocatedScanner);
////                });
//            }
//        }

        return map;
    }

    private void mapScanner(Scanner scanner, int xOffset, int yOffset, int zOffset, Set<Point3D> map) {
        scanner.setPosition(xOffset, yOffset, zOffset);
        map.addAll(scanner.getBeaconPoints().stream().map(point -> new Point3D(point.getX() + xOffset, point.getY() + yOffset, point.getZ() + zOffset))
                .collect(Collectors.toList()));
    }

    private void mapScanner(Scanner scanner, Set<Point3D> overlappingBeacons, Set<Point3D> map) {
        Point3D offset = point3DSubtract(map.stream().findFirst().get(), overlappingBeacons.stream().findFirst().get());
        scanner.setPosition(offset.getX(), offset.getY(), offset.getZ());
        map.addAll(overlappingBeacons.stream().map(point -> new Point3D(point.getX() + offset.getX(), point.getY() + offset.getY(), point.getZ() + offset.getZ()))
                .collect(Collectors.toList()));
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
                tmp.add(new Scanner(scannerID, input.subList(startIndex, endIndex + 1).stream().filter(point -> !point.isBlank()).collect(Collectors.toList())));
            }
            endIndex++;
        }

        return tmp;
    }

    private Set<Point3D> scannerOverlapsMap(Set<Point3D> map, Scanner scanner, int overlapLimit) {
        Map<Point3D, Set<Point3D>> temp = new LinkedHashMap<>();
        Map<Point3D, Long> tempx = new LinkedHashMap<>();

        for (Point3D p : map) {
            for (int i = 1; i < 25; i++) {
                for (Point3D sp : scanner.orientate(i)) {
                    Point3D key = point3DSubtract(p, sp);
                    Optional<Point3D> check = temp.keySet().stream().filter(k -> k.equals(key)).findFirst();

                    if (check.isPresent()) {
                        temp.get(key).add(sp);

                        tempx.put(check.get(), tempx.getOrDefault(check.get(), 0L) + 1L);

//                        if (temp.get(check.get()) >= overlapLimit) {
//                            return check;
//                        }
                    } else {
                        temp.put(key, new LinkedHashSet<>());
                        temp.get(key).add(sp);

                        tempx.put(key, 1L);
                    }
                }
            }
        }

        var testx = tempx.entrySet().stream().filter(entry -> entry.getValue() >= overlapLimit).collect(Collectors.toList());
        var filtered = temp.entrySet().stream().filter(entry -> entry.getValue().size() >= overlapLimit).findAny().map(Map.Entry::getValue).orElse(null);

        return filtered;
    }

    private Point3D point3DSubtract(Point3D a, Point3D b) {
        return new Point3D(a.getX() - b.getX(),
                a.getY() - b.getY(),
                a.getZ() - b.getZ());
    }

    class Scanner {
        private int id;
        private Point3D position;
        private List<Point3D> beaconPositions = new LinkedList<>();
        private Map<Scanner, Set<Point3D>> map = new LinkedHashMap<>();

        public Scanner(int id, List<String> beaconStringPositions) {
            this.id = id;

            for (String s : beaconStringPositions) {
                var split = s.split(",");

                beaconPositions.add(new Point3D(Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2])));
            }
        }

        public Map<Scanner, Set<Point3D>> getMap() {
            return map;
        }

        public int getID() {
            return id;
        }

        public void mapScanner(Scanner scanner, Set<Point3D> points) {
            map.put(scanner, points);
        }

        public void setPosition(int x, int y, int z) {
            this.position = new Point3D(x, y, z);
        }

        public Set<Point3D> orientate(int orientation) {
            switch (orientation) {
                case 1 -> { // x, y, z
                    return beaconPositions.stream().map(point -> new Point3D(point.getX(), point.getY(), point.getZ())).collect(Collectors.toSet());
                }
                case 2 -> { // x, -y, -z
                    return beaconPositions.stream().map(point -> new Point3D(point.getX(), -point.getY(), -point.getZ())).collect(Collectors.toSet());
                }
                case 3 -> { // x, z, -y
                    return beaconPositions.stream().map(point -> new Point3D(point.getX(), point.getZ(), -point.getY())).collect(Collectors.toSet());
                }
                case 4 -> { // x, -z, y
                    return beaconPositions.stream().map(point -> new Point3D(point.getX(), -point.getZ(), point.getY())).collect(Collectors.toSet());
                }
                case 5 -> { // -x, y, -z
                    return beaconPositions.stream().map(point -> new Point3D(-point.getX(), point.getY(), -point.getZ())).collect(Collectors.toSet());
                }
                case 6 -> { // -x, -y, z
                    return beaconPositions.stream().map(point -> new Point3D(-point.getX(), -point.getY(), point.getZ())).collect(Collectors.toSet());
                }
                case 7 -> { // -x, z, y
                    return beaconPositions.stream().map(point -> new Point3D(-point.getX(), point.getZ(), point.getY())).collect(Collectors.toSet());
                }
                case 8 -> { // -x, -z, -y
                    return beaconPositions.stream().map(point -> new Point3D(-point.getX(), -point.getZ(), -point.getY())).collect(Collectors.toSet());
                }
                case 9 -> { // y, x, -z
                    return beaconPositions.stream().map(point -> new Point3D(point.getY(), point.getX(), -point.getZ())).collect(Collectors.toSet());
                }
                case 10 -> { // y, -x, z
                    return beaconPositions.stream().map(point -> new Point3D(point.getY(), -point.getX(), point.getZ())).collect(Collectors.toSet());
                }
                case 11 -> { // y, z, x
                    return beaconPositions.stream().map(point -> new Point3D(point.getY(), point.getZ(), point.getX())).collect(Collectors.toSet());
                }
                case 12 -> { // y, -z, -x
                    return beaconPositions.stream().map(point -> new Point3D(point.getY(), -point.getZ(), -point.getX())).collect(Collectors.toSet());
                }
                case 13 -> { // -y, x, z
                    return beaconPositions.stream().map(point -> new Point3D(-point.getY(), point.getX(), point.getZ())).collect(Collectors.toSet());
                }
                case 14 -> { // -y, -x, -z
                    return beaconPositions.stream().map(point -> new Point3D(-point.getY(), -point.getX(), -point.getZ())).collect(Collectors.toSet());
                }
                case 15 -> { // -y, z, -x
                    return beaconPositions.stream().map(point -> new Point3D(-point.getY(), point.getZ(), -point.getX())).collect(Collectors.toSet());
                }
                case 16 -> { // -y, -z, x
                    return beaconPositions.stream().map(point -> new Point3D(-point.getY(), -point.getZ(), point.getX())).collect(Collectors.toSet());
                }
                case 17 -> { // z, y, -x
                    return beaconPositions.stream().map(point -> new Point3D(point.getZ(), point.getY(), -point.getX())).collect(Collectors.toSet());
                }
                case 18 -> { // z, -y, x
                    return beaconPositions.stream().map(point -> new Point3D(point.getZ(), -point.getY(), point.getX())).collect(Collectors.toSet());
                }
                case 19 -> { // z, x, y
                    return beaconPositions.stream().map(point -> new Point3D(point.getZ(), point.getX(), point.getY())).collect(Collectors.toSet());
                }
                case 20 -> { // z, -x, -y
                    return beaconPositions.stream().map(point -> new Point3D(point.getZ(), -point.getX(), -point.getY())).collect(Collectors.toSet());
                }
                case 21 -> { // -z, x, -y
                    return beaconPositions.stream().map(point -> new Point3D(-point.getZ(), point.getX(), -point.getY())).collect(Collectors.toSet());
                }
                case 22 -> { // -z, -x, y
                    return beaconPositions.stream().map(point -> new Point3D(-point.getZ(), -point.getX(), point.getY())).collect(Collectors.toSet());
                }
                case 23 -> { // -z, y, x
                    return beaconPositions.stream().map(point -> new Point3D(-point.getZ(), point.getY(), point.getX())).collect(Collectors.toSet());
                }
                case 24 -> { // -z, -y, -x
                    return beaconPositions.stream().map(point -> new Point3D(-point.getZ(), -point.getY(), -point.getX())).collect(Collectors.toSet());
                }
                default -> {
                    System.out.println("Error...");
                    return null;
                }
            }
        }

        public List<Point3D> getBeaconPoints() {
            return beaconPositions;
        }

        public Set<Point3D> calculateOverlappingBeacons(Scanner other) {
            Map<Point3D, Set<Point3D>> temp = new LinkedHashMap<>();

            for (Point3D p : beaconPositions) {
                for (int i = 1; i < 25; i++) {
                    for (Point3D sp : other.orientate(i)) {
                        Point3D key = point3DSubtract(p, sp);
                        Optional<Point3D> check = temp.keySet().stream().filter(k -> k.equals(key)).findFirst();

                        if (check.isPresent()) {
                            temp.get(key).add(sp);
                        } else {
                            temp.put(key, new LinkedHashSet<>());
                            temp.get(key).add(sp);
                        }
                    }
                }
            }

            var filtered = temp.entrySet().stream().filter(entry -> entry.getValue().size() >= 12).findAny().map(Map.Entry::getValue).orElse(new LinkedHashSet<>());
            return filtered;
        }
    }

    class Point3D implements Comparable<Point3D> {
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
        public int compareTo(Point3D o) {
            return this.equals(o) ? 0 : Math.abs(this.x - o.x + this.y - o.y + this.z - o.z);
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