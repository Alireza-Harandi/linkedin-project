package com.example.dead.model;

import java.util.*;

public class UnGraph<V, E> {
    private Map<V, Map<V, E>> adjList;
    private int numEdges;

    public UnGraph() {
        adjList = new HashMap<>();
        numEdges = 0;
    }

    public Map<V, E> getOrDefault(V v){
        return adjList.getOrDefault(v, new HashMap<>());
    }

    public int numVertices() {
        return adjList.size();
    }

    public Set<V> vertices() {
        return adjList.keySet();
    }

    public int numEdges() {
        return numEdges;
    }

    public Set<E> edges() {
        Set<E> edges = new HashSet<>();
        for (Map<V, E> neighbors : adjList.values()) {
            edges.addAll(neighbors.values());
        }
        return edges;
    }

    public E getEdge(V u, V v) {
        if (adjList.containsKey(u) && adjList.get(u).containsKey(v)) {
            return adjList.get(u).get(v);
        }
        return null;
    }

    public List<V> endVertices(E e) {
        for (Map.Entry<V, Map<V, E>> entry : adjList.entrySet()) {
            V u = entry.getKey();
            for (Map.Entry<V, E> neighbor : entry.getValue().entrySet()) {
                if (neighbor.getValue().equals(e)) {
                    return Arrays.asList(u, neighbor.getKey());
                }
            }
        }
        return null;
    }

    public V opposite(V v, E e) {
        if (adjList.containsKey(v)) {
            for (Map.Entry<V, E> neighbor : adjList.get(v).entrySet()) {
                if (neighbor.getValue().equals(e)) {
                    return neighbor.getKey();
                }
            }
        }
        return null;
    }

    public int outDegree(V v) {
        if (adjList.containsKey(v)) {
            return adjList.get(v).size();
        }
        return 0;
    }

    public int inDegree(V v) {
        return outDegree(v);
    }

    public Set<E> outgoingEdges(V v) {
        if (adjList.containsKey(v)) {
            return new HashSet<>(adjList.get(v).values());
        }
        return Collections.emptySet();
    }

    public Set<E> incomingEdges(V v) {
        return outgoingEdges(v);
    }

    public void insertVertex(V v) {
        adjList.putIfAbsent(v, new HashMap<>());
    }

    public void insertEdge(V u, V v, E e) {
        adjList.putIfAbsent(u, new HashMap<>());
        adjList.putIfAbsent(v, new HashMap<>());
        if (!adjList.get(u).containsKey(v)) {
            adjList.get(u).put(v, e);
            adjList.get(v).put(u, e);
            numEdges++;
        }
    }

    public void removeVertex(V v) {
        if (adjList.containsKey(v)) {
            for (V neighbor : adjList.get(v).keySet()) {
                adjList.get(neighbor).remove(v);
                numEdges--;
            }
            adjList.remove(v);
        }
    }

    public void removeEdge(E e) {
        Set<V> verticesToRemove = new HashSet<>();

        for (Map.Entry<V, Map<V, E>> entry : adjList.entrySet()) {
            V u = entry.getKey();
            Map<V, E> neighbors = entry.getValue();

            for (Map.Entry<V, E> neighbor : neighbors.entrySet()) {
                if (neighbor.getValue().equals(e)) {
                    V v = neighbor.getKey();
                    verticesToRemove.add(u);
                    verticesToRemove.add(v);
                }
            }
        }

        for (V vertex : verticesToRemove) {
            adjList.get(vertex).values().removeIf(edge -> edge.equals(e));
        }
        numEdges--;
    }

    public List<Pair<V, Integer>> bfs(V start) {
        List<Pair<V, Integer>> result = new ArrayList<>();
        if (!adjList.containsKey(start))
            return result;

        Set<V> visited = new HashSet<>();
        Queue<Pair<V, Integer>> queue = new LinkedList<>();
        visited.add(start);

        for (V neighbor : adjList.get(start).keySet()) {
            queue.add(new Pair<>(neighbor, 0));
            visited.add(neighbor);
        }

        while (!queue.isEmpty() && result.size() < 20) {
            Pair<V, Integer> currentPair = queue.poll();
            V current = currentPair.getKey();
            int level = currentPair.getValue();

            if (level > 0)
                result.add(new Pair<>(current, level));

            for (V neighbor : adjList.get(current).keySet()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(new Pair<>(neighbor, level + 1));
                }
            }
        }

        return result;
    }


    public void removeEdgeBetween(V u, V v) {
        if (adjList.containsKey(u) && adjList.get(u).containsKey(v)) {
            adjList.get(u).remove(v);
            adjList.get(v).remove(u);
            numEdges--;
        }
    }

}
