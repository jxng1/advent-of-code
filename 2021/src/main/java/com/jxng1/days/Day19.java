package main.java.com.jxng1.days;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Day19 extends Day {
    public Day19(int day) {
        super(day);
    }

    @Override
    String task1(List<String> input) {
        Beacon a = new Beacon(new Point3D(-618, -824, -621));
        Beacon b = new Beacon(new Point3D(-537, -823, -458));
        Beacon c = new Beacon(new Point3D(-200, 500, 300));
        Scanner tmp = new Scanner(0, new Point3D(0, 0, 0));
        tmp.addBeacon(a);
        tmp.addBeacon(b);
        tmp.addBeacon(c);

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
        private List<Beacon> beacons = new LinkedList<>();
        private int[][] beaconDistanceMatrix;

        public Scanner(int id, Point3D location) {
            this.id = id;
            this.location = location;
        }

        public void addBeacon(Beacon beacon) {
            beacons.add(beacon);
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