package main.java.com.jxng1.days;

import main.java.com.jxng1.util.InputReader;

import java.util.*;
import java.util.function.Function;
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

                scannerOverlapsMap(map, unlocatedScanner, 12).ifPresent(offset -> {
                    mapScanner(unlocatedScanner, offset.getX(), offset.getY(), offset.getZ(), map);
                    locatedScanners.add(unlocatedScanner);
                    System.out.println("Scanner found: " + unlocatedScanner.id);
                    System.out.println(locatedScanners.size());
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
                tmp.add(new Scanner(scannerID, input.subList(startIndex, endIndex + 1).stream().filter(pos -> !pos.isBlank()).collect(Collectors.toList())));
            }
            endIndex++;
        }

        return tmp;
    }

    private Optional<Point3D> scannerOverlapsMap(Set<Point3D> map, Scanner scanner, int overlapLimit) {
        Map<Point3D, Long> temp = new LinkedHashMap<>();

        for (Point3D p : map) {
            for (Point3D sp : scanner.orientate()) {
                Point3D key = point3DSubtract(p, sp);
                Optional<Point3D> check = temp.keySet().stream().filter(k -> k.getX() == key.getX() && k.getY() == key.getY() && k.getZ() == key.getZ()).findFirst();

                if (check.isPresent()) {
                    temp.put(check.get(), temp.getOrDefault(check.get(), 0L) + 1L);

                    if (temp.get(check.get()) >= overlapLimit) {
                        return check;
                    }
                } else {
                    temp.put(key, 1L);
                }
            }
        }

        return temp.entrySet().stream().filter(entry -> entry.getValue() >= overlapLimit).findFirst().map(Map.Entry::getKey);
    }

    private Map<Integer, Long> distanceMap(Set<Point3D> map, Scanner scanner) {
        var temp = map.stream().flatMap(mapPoint -> scanner.getBeaconPoints().stream().map(scannerPoint -> {
            return (mapPoint.getX() - scannerPoint.getX() + mapPoint.getY() - scannerPoint.getY() + mapPoint.getZ() - scannerPoint.getZ());
        })).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return temp;
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

        public Set<Point3D> orientate() {
            Set<Point3D> ret = new LinkedHashSet<>();

            for (Point3D pos : beaconPositions) {
                ret.add(new Point3D(pos.getX(), pos.getY(), pos.getZ()));
                ret.add(new Point3D(pos.getX(), -pos.getY(), -pos.getZ()));
                ret.add(new Point3D(pos.getX(), pos.getZ(), -pos.getY()));
                ret.add(new Point3D(pos.getX(), -pos.getZ(), pos.getY()));
                ret.add(new Point3D(-pos.getX(), pos.getY(), -pos.getZ()));
                ret.add(new Point3D(-pos.getX(), -pos.getY(), pos.getZ()));
                ret.add(new Point3D(-pos.getX(), pos.getZ(), pos.getY()));
                ret.add(new Point3D(-pos.getX(), -pos.getZ(), -pos.getY()));
                ret.add(new Point3D(pos.getY(), pos.getX(), -pos.getZ()));
                ret.add(new Point3D(pos.getY(), -pos.getX(), pos.getZ()));
                ret.add(new Point3D(pos.getY(), pos.getZ(), pos.getX()));
                ret.add(new Point3D(pos.getY(), -pos.getZ(), -pos.getX()));
                ret.add(new Point3D(-pos.getY(), pos.getX(), pos.getZ()));
                ret.add(new Point3D(-pos.getY(), -pos.getX(), -pos.getZ()));
                ret.add(new Point3D(-pos.getY(), pos.getZ(), -pos.getX()));
                ret.add(new Point3D(-pos.getY(), -pos.getZ(), pos.getX()));
                ret.add(new Point3D(pos.getZ(), pos.getY(), -pos.getX()));
                ret.add(new Point3D(pos.getZ(), -pos.getY(), pos.getX()));
                ret.add(new Point3D(pos.getZ(), pos.getX(), pos.getY()));
                ret.add(new Point3D(pos.getZ(), -pos.getX(), -pos.getY()));
                ret.add(new Point3D(-pos.getZ(), pos.getX(), -pos.getY()));
                ret.add(new Point3D(-pos.getZ(), -pos.getX(), pos.getY()));
                ret.add(new Point3D(-pos.getZ(), pos.getY(), pos.getX()));
                ret.add(new Point3D(-pos.getZ(), -pos.getY(), -pos.getX()));
            }

            return ret;
        }

        public List<Point3D> getBeaconPoints() {
            return beaconPositions;
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