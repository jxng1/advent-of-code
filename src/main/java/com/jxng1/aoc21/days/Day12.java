package main.java.com.jxng1.aoc21.days;

import main.java.com.jxng1.Day;

import java.util.*;
import java.util.stream.Collectors;

public class Day12 extends Day {
    public Day12(int day) {
        super(day);
    }

    @Override
    protected String task1(List<String> input) {
        Graph graph = generateGraph(input);
        List<String> paths = new LinkedList<>();
        Map<String, Integer> vertexVisitMap = graph.getAdjVertices().keySet().stream()
                .filter(key -> key.getLabel().matches("[a-z]+")
                        && !key.getLabel().equals("start")
                        && !key.getLabel().equals("end"))
                .collect(Collectors.toMap(Vertex::getLabel, value -> 0));

        for (var neighbour : graph.getNeighboursOfVertex("start")) {
            task1DFS(
                    graph
                    , neighbour.getLabel()
                    , new HashMap<>(vertexVisitMap)
                    , "start"
                    , paths
            );
        }

        //paths.forEach(path -> System.out.println(String.join(",", path)));

        return String.valueOf(paths.size());
    }

    @Override
    protected String task2(List<String> input) {
        Graph graph = generateGraph(input);
        List<String> paths = new LinkedList<>();
        Map<String, Integer> vertexVisitMap = graph.getAdjVertices().keySet().stream()
                .filter(key -> key.getLabel().matches("[a-z]+")
                        && !key.getLabel().equals("start")
                        && !key.getLabel().equals("end"))
                .collect(Collectors.toMap(Vertex::getLabel, value -> 0));

        for (var neighbour : graph.getNeighboursOfVertex("start")) {
            task2DFS(
                    graph
                    , neighbour.getLabel()
                    , new HashMap<>(vertexVisitMap)
                    , "start"
                    , paths
                    , false
            );
        }

        //paths.forEach(path -> System.out.println(String.join(",", path)));

        return String.valueOf(paths.size());
    }

    private Graph generateGraph(List<String> input) {
        Graph ret = new Graph();

        for (String line : input) {
            var split = line.split("-");
            ret.addVertex(split[0]);
            ret.addVertex(split[1]);
            ret.addEdge(split[0], split[1]);
        }

        return ret;
    }

    private void task1DFS(Graph graph, String vertex, Map<String, Integer> vertexVisitAmounts, String currentPath, List<String> paths) {
        currentPath += "," + vertex;

        if (vertex.equals("end")) {
            paths.add(currentPath);
            return;
        } else if (vertex.matches("[a-z]+")) { // small cave
            vertexVisitAmounts.put(vertex, vertexVisitAmounts.getOrDefault(vertex, 0) + 1);
        }

        for (Vertex neighbour : graph.getNeighboursOfVertex(vertex)) {
            if (!neighbour.getLabel().equals("start") && vertexVisitAmounts.getOrDefault(neighbour.getLabel(), 0) == 0) {
                task1DFS(graph, neighbour.getLabel(), new HashMap<>(vertexVisitAmounts), currentPath, paths);
            }
        }
    }

    private void task2DFS(Graph graph, String vertex, Map<String, Integer> vertexVisitAmounts, String currentPath, List<String> paths, boolean sameVertexVisitedTwice) {
        currentPath += "," + vertex;

        if (vertex.equals("end")) {
            paths.add(currentPath);
            return;
        } else if (vertex.matches("[a-z]+")) { // If current cave is small
            vertexVisitAmounts.put(vertex, vertexVisitAmounts.get(vertex) + 1);
            int currentNodeVisits = vertexVisitAmounts.get(vertex);

            // if node has been visited twice on current path and num of current node is more than 2, early return
            if (currentNodeVisits >= 2 && sameVertexVisitedTwice) {
                return;
            } else if (currentNodeVisits == 2) { // if node visits == 2, set boolean to true
                sameVertexVisitedTwice = true;
            }
        }

        for (Vertex neighbour : graph.getNeighboursOfVertex(vertex)) {
            if (!neighbour.getLabel().equals("start") && // cave not "start"
                    (vertexVisitAmounts.getOrDefault(neighbour.getLabel(), 0) == 0 || // cave can be visited multiple times(e.g. capital letter caves)
                            (vertexVisitAmounts.getOrDefault(neighbour.getLabel(), 1) == 1 && !sameVertexVisitedTwice))) { // cave can be visited twice(only one time)
                task2DFS(graph, neighbour.getLabel(), new HashMap<>(vertexVisitAmounts), currentPath, paths, sameVertexVisitedTwice);
            }
        }
    }
}

class Graph {
    private Map<Vertex, List<Vertex>> adjVertices;

    public Graph() {
        this.adjVertices = new LinkedHashMap<>();
    }

    Map<Vertex, List<Vertex>> getAdjVertices() {
        return adjVertices;
    }

    List<Vertex> getNeighboursOfVertex(String label) {
        return adjVertices.get(adjVertices.keySet().stream().filter(v -> v.getLabel().equals(label)).findFirst().get());
    }

    Vertex getVertex(String label) {
        return adjVertices.keySet().stream().filter(v -> v.getLabel().equals(label)).findFirst().get();
    }

    void addVertex(String label) {
        var query = adjVertices.keySet().stream().filter(vertex -> vertex.getLabel().equals(label)).findFirst();

        if (query.isEmpty()) {
            adjVertices.put(new Vertex(label), new ArrayList<>());
        }
    }

    void removeVertex(String label) {
        Vertex v = new Vertex(label);

        adjVertices.values().forEach(e -> e.remove(v));
        adjVertices.remove(v);
    }

    void addEdge(String label1, String label2) {
        Vertex v1 = adjVertices.keySet().stream().filter(v -> v.getLabel().equals(label1)).findFirst().get();
        Vertex v2 = adjVertices.keySet().stream().filter(v -> v.getLabel().equals(label2)).findFirst().get();
        adjVertices.get(v1).add(v2);
        adjVertices.get(v2).add(v1);
    }

    void removeEdge(String label1, String label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        List<Vertex> eV1 = adjVertices.get(v1);
        List<Vertex> eV2 = adjVertices.get(v2);
        if (eV1 != null)
            eV1.remove(v2);
        if (eV2 != null)
            eV2.remove(v1);
    }
}

class Vertex {
    private String label;

    Vertex(String label) {
        this.label = label;
    }

    String getLabel() {
        return label;
    }
}