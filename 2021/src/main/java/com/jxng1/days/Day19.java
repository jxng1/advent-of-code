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

    private Set<Point3D> solve(LinkedList<Scanner> scanners) {
        for (Scanner a : scanners) {
            for (Scanner b : scanners) {
                if (a.getID() == b.getID() || a.getMap().containsKey(b) || b.getMap().containsKey(a)) {
                    continue;
                }
                if (a.getMap().get(b) != null && b.getMap().get(a) != null) {
                    continue;
                }

                var entryOptional = a.calculateOverlappingBeacons(b);

                entryOptional.ifPresent(entry -> {
                    a.mapScanner(b, entry.getKey(), entry.getValue());
                    b.mapScanner(a, entry.getKey(), entry.getValue());
                });
            }
        }

        return dfsSearch(scanners);
    }

    private void mapScanner(Scanner scanner, Fingerprint print, Set<Point3D> map) {
        Point3D offset = print.getOffset();
        scanner.setPosition(offset.getX(), offset.getY(), offset.getZ());
        map.addAll(print.getOverlappingPoints().stream().map(point -> new Point3D(point.getX() + offset.getX(), point.getY() + offset.getY(), point.getZ() + offset.getZ()))
                .collect(Collectors.toList()));
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
                tmp.add(new Scanner(scannerID, input.subList(startIndex, endIndex + 1).stream().filter(point -> !point.isBlank()).collect(Collectors.toList())));
            }
            endIndex++;
        }

        return tmp;
    }

    private Point3D point3DSubtract(Point3D a, Point3D b) {
        return new Point3D(a.getX() - b.getX(),
                a.getY() - b.getY(),
                a.getZ() - b.getZ());
    }

    private Set<Point3D> dfsSearch(LinkedList<Scanner> unsolved) {
        Queue<Scanner> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[unsolved.size()];

        Scanner base = unsolved.get(0);
        visited[base.getID()] = true;
        queue.add(base);
        Set<Point3D> map = new HashSet<>(base.getBeaconPoints());
        while (!queue.isEmpty()) {
            var popped = queue.remove();
            System.out.println(popped.getID());

            for (Scanner neighbour : popped.getMap().keySet()) {
                if (!visited[neighbour.getID()]) {
                    visited[neighbour.getID()] = true;
                    mapScanner(neighbour, popped.getMap().get(neighbour), map);
                    queue.add(neighbour);
                }
            }
        }


        return map;
    }

    class Scanner {
        private int id;
        private Point3D position;
        private List<Point3D> beaconPositions = new LinkedList<>();
        private Map<Scanner, Fingerprint> map = new LinkedHashMap<>();

        public Scanner(int id, List<String> beaconStringPositions) {
            this.id = id;

            for (String s : beaconStringPositions) {
                var split = s.split(",");

                beaconPositions.add(new Point3D(Integer.parseInt(split[0]),
                        Integer.parseInt(split[1]),
                        Integer.parseInt(split[2])));
            }
        }

        public Map<Scanner, Fingerprint> getMap() {
            return map;
        }

        public int getID() {
            return id;
        }

        public void mapScanner(Scanner scanner, Point3D offset, Set<Point3D> points) {
            map.put(scanner, new Fingerprint(offset, points));
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

        public Optional<Map.Entry<Point3D, Set<Point3D>>> calculateOverlappingBeacons(Scanner other) {
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

//                            if (temp.get(key).size() >= 12) {
//                                return temp.get(key);
//                            }
                        }
                    }
                }
            }

            //var keyFilter = temp.entrySet().stream().filter(entry -> entry.getValue().size() >= 12).findAny().map(Map.Entry::getKey).get();
            var filtered = temp.entrySet().stream().filter(entry -> entry.getValue().size() >= 12).findAny().map(Map.Entry::getValue).orElse(new LinkedHashSet<>());

            return temp.entrySet().stream().filter(entry -> entry.getValue().size() >= 12).findFirst();
        }
    }

    class Fingerprint {
        private Point3D offset;
        private Set<Point3D> overlappingPoints;

        public Fingerprint(Point3D offset, Set<Point3D> overlappingPoints) {
            this.offset = offset;
            this.overlappingPoints = overlappingPoints;
        }

        public Point3D getOffset() {
            return offset;
        }

        public Set<Point3D> getOverlappingPoints() {
            return overlappingPoints;
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