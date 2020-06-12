package ir.imorate.graph;

import lombok.Data;

import java.util.*;

@Data
public class Graph<T> {
    private Map<Node<T>, LinkedList<Node<T>>> adjacencyMap;

    public Graph() {
        this.adjacencyMap = new LinkedHashMap<>();
    }

    public Boolean insert(T source, T destination) {
        Node<T> sourceNode = new Node<>(source);
        Node<T> destNode = new Node<>(destination);
        if (!this.adjacencyMap.containsKey(sourceNode)) {
            this.adjacencyMap.put(sourceNode, new LinkedList<>());
        }
        if (!this.adjacencyMap.containsKey(destNode)) {
            this.adjacencyMap.put(destNode, new LinkedList<>());
        }
        LinkedList<Node<T>> nodes = this.adjacencyMap.get(sourceNode);
        if (!nodes.contains(destNode)) {
            nodes.add(destNode);
            this.adjacencyMap.put(sourceNode, nodes);
        }
        return true;
    }

    public boolean isBidirectional(T source, T destination) {
        return hasEdge(source, destination) && hasEdge(destination, source);
    }

    public boolean hasEdge(T source, T destination) {
        Node<T> sourceNode = new Node<>(source);
        Node<T> destNode = new Node<>(destination);
        return adjacencyMap.containsKey(sourceNode) && adjacencyMap.get(sourceNode) != null &&
                adjacencyMap.get(sourceNode).contains(destNode);
    }

    public boolean bfsTraverse(T source, T destination) {
        Node<T> sourceNode = new Node<>(source);
        Node<T> destNode = new Node<>(destination);
        List<Node<T>> visitedNodes = new ArrayList<>();
        Queue<Node<T>> queue = new LinkedList<>();
        queue.add(sourceNode);
        while (!queue.isEmpty()) {
            Node<T> visitedNode = queue.remove();
            visitedNodes.add(visitedNode);
            List<Node<T>> neighbours = adjacencyMap.get(visitedNode);
            if (neighbours != null) {
                for (Node<T> neighbourNode : neighbours) {
                    if (neighbourNode.equals(destNode)) {
                        visitedNodes.add(neighbourNode);
                        return true;
                    } else if (!visitedNodes.contains(neighbourNode)) {
                        queue.add(neighbourNode);
                    }
                }
            }
        }
        return false;
    }

    public boolean hasBidirectionalTraverse(T source, T destination) {
        return bfsTraverse(source, destination) && bfsTraverse(destination, source);
    }
}
