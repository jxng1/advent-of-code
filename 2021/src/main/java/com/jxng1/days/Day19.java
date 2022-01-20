package main.java.com.jxng1.days;

import java.util.*;

public class Day19 extends Day {
    public Day19(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        Beacon a = new Beacon(new Point3D(-618, -824, -621));
        Beacon b = new Beacon(new Point3D(-537, -823, -458));
        Scanner tmp = new Scanner(0, new Point3D(0, 0, 0));
        tmp.addBeacon(a);
        tmp.addBeacon(b);

        tmp.calculateBeaconsDistances();


        return null;
    }

    @Override
    String task2(List<String> input) {
        return null;
    }

    class Scanner {
        private Point3D location;
        int id;
        private Set<Beacon> beacons = new HashSet<>();

        public Scanner(int id, Point3D location) {
            this.id = id;
            this.location = location;
        }

        public void addBeacon(Beacon beacon) {
            beacons.add(beacon);
        }

        public void calculateBeaconsDistances() {
            for (Beacon a : beacons) {
                for (Beacon b : beacons) {
                    if (a.equals(b)) continue;

                    a.linkBeacon(b);
                }
            }
        }
    }

    class Beacon {
        private Point3D location;
        private Map<Beacon, Integer> beaconDistanceMap = new HashMap<>();

        public Beacon(Point3D location) {
            this.location = location;
        }

        public void linkBeacon(Beacon beacon) {
            beaconDistanceMap.put(beacon, location.calculateStraightLineDistance(beacon.location));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Beacon beacon = (Beacon) o;
            return Objects.equals(location, beacon.location) && Objects.equals(beaconDistanceMap, beacon.beaconDistanceMap);
        }

        @Override
        public int hashCode() {
            return Objects.hash(location, beaconDistanceMap);
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

        public int calculateStraightLineDistance(Point3D other) {
            return (int) Math.sqrt(Math.pow(this.x - other.x, 2)
                    + Math.pow(this.y - other.y, 2)
                    + Math.pow(this.z - other.z, 2));
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