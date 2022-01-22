package main.java.com.jxng1.days;

import main.java.com.jxng1.util.InputReader;

import java.util.*;

public class Day19 extends Day {
    public Day19(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        //Map<Scanner, Set<Scanner>> scannerListMap = parseScanners(input);
        List<String> sample = InputReader.getInputReader().getInputAsList("day19sample.txt");
        var scannerListMap = parseScanners(sample);

        for (Scanner a : scannerListMap.keySet()) {
            for (Scanner b : scannerListMap.keySet()) {
                if (a.equals(b) || scannerListMap.get(a).contains(b) || scannerListMap.get(b).contains(a)) {
                    continue;
                }

                // iterate over distance matrix and see if b matches any of them, if so, increment sharedBeaconEdgeCount
                int sharedBeaconEdgeCount = 0;
                int[][] aBeaconDistanceMatrix = a.getBeaconDistanceMatrix();
                for (int i = 0; i < aBeaconDistanceMatrix.length; i++) {
                    for (int j = i + 1; j < aBeaconDistanceMatrix[i].length; j++) {
                        sharedBeaconEdgeCount = b.sharesBeaconToBeaconEdge(aBeaconDistanceMatrix[i][j]) ? sharedBeaconEdgeCount + 1 : sharedBeaconEdgeCount;

                        if (sharedBeaconEdgeCount >= 12) {
                            scannerListMap.get(a).add(b);
                            scannerListMap.get(b).add(a);
                            break;
                        }
                    }
                    if (sharedBeaconEdgeCount >= 12) {
                        break;
                    }
                }
            }
        }

        return null;
    }

    private Map<Scanner, Set<Scanner>> parseScanners(List<String> input) {
        Map<Scanner, Set<Scanner>> ret = new HashMap<>();

        int startIndex = 0;
        int endIndex = 0;
        int scannerID = 0;
        for (String s : input) {
            if (s.contains("--- scanner ")) {
                startIndex = endIndex;
                scannerID = Integer.parseInt(s.substring(12, 13));
            } else if (s.isBlank() || endIndex == input.size() - 1) {
                if (scannerID == 0) { // all positions are relative to scanner 0 so it must have a position of 0, 0, 0...
                    ret.put(
                            parseScanner(input.subList(startIndex + 1, endIndex), scannerID, new Point3D(0, 0, 0))
                            , new HashSet<>());
                } else {
                    ret.put(
                            parseScanner(input.subList(startIndex + 1, endIndex), scannerID, new Point3D(-1, -1, -1))
                            , new HashSet<>());
                }
            }
            endIndex++;
        }

        return ret;
    }

    @Override
    String task2(List<String> input) {
        return null;
    }

    private Scanner parseScanner(List<String> beaconsLocationList, int id, Point3D scannerLocation) {
        Scanner scanner = new Scanner(id, scannerLocation);

        for (String s : beaconsLocationList) {
            var coordinates = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
            Beacon tmp = new Beacon(new Point3D(coordinates[0], coordinates[1], coordinates[2]));

            scanner.addBeacon(tmp);
        }
        scanner.calculateBeaconsDistances();

        return scanner;
    }

    class Scanner {
        private Point3D location;
        int id;
        private List<Beacon> beacons = new LinkedList<>();
        private int[][] beaconDistanceMatrix;

        public Scanner(int id, Point3D location) {
            this.id = id;
            this.location = location;
        }

        public int[][] getBeaconDistanceMatrix() {
            return beaconDistanceMatrix;
        }

        public void addBeacon(Beacon beacon) {
            beacons.add(beacon);
        }

        public boolean sharesBeaconToBeaconEdge(int edgeValue) {
            for (int[] row : beaconDistanceMatrix) {
                for (int rowCol : row) {
                    if (rowCol == edgeValue) {
                        return true;
                    }
                }
            }

            return false;
        }

        public void calculateBeaconsDistances() {
            int[][] tmp = new int[beacons.size()][beacons.size()];

            for (int i = 0; i < beacons.size(); i++) {
                for (int j = i + 1; j < beacons.size(); j++) {
                    Beacon a = beacons.get(i);
                    Beacon b = beacons.get(j);

                    if (!a.equals(b)) {
                        int ret = a.calculateStraightLineDistance(b);

                        tmp[i][j] = tmp[j][i] = ret;
                    }
                }
            }

            beaconDistanceMatrix = tmp;
        }
    }

    class Beacon {
        private Point3D location;

        public Beacon(Point3D location) {
            this.location = location;
        }

        public Point3D getLocation() {
            return location;
        }

        public int calculateStraightLineDistance(Beacon other) {
            var x = Math.pow(this.location.getX() - other.location.getX(), 2);
            var y = Math.pow(this.location.getY() - other.location.getY(), 2);
            var z = Math.pow(this.location.getZ() - other.location.getZ(), 2);

            var ret = (int) Math.sqrt(x + y + z);

            return (int) Math.sqrt(Math.pow(this.location.getX() - other.location.getX(), 2)
                    + Math.pow(this.location.getY() - other.location.getY(), 2)
                    + Math.pow(this.location.getZ() - other.location.getZ(), 2));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beacon beacon = (Beacon) o;
            return Objects.equals(location, beacon.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location);
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
        public String
        toString() {
            return "Point3D{" +
                    "x=" + x +
                    ", y=" + y +
                    ", z=" + z +
                    '}';
        }
    }
}