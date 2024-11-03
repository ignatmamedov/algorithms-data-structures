package nl.saxion.cds.solution;

import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyGraph<V> implements SaxGraph<V> {

    private MyHashMap<V, MyArrayList<DirectedEdge<V>>> map = new MyHashMap<>();
    /**
     * Determines if the collection has no elements
     *
     * @return if the collection has no elements
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Determines the number of elements in this collection
     *
     * @return size of this collection
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * Create a String representation of the data in GraphViz (see <a href="https://graphviz.org">GraphViz</a>)
     * format, which you can print-copy-paste on the site see <a href="https://dreampuf.github.io/GraphvizOnline">GraphViz online</a>.
     *
     * @param name name of the produced graph
     * @return a GraphViz string representation of this collection
     */
    @Override
    public String graphViz(String name) {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph ").append(name).append(" {\n");

        for (V vertex : map.getKeys()) {
            for (DirectedEdge<V> edge : map.get(vertex)) {
                builder.append("    \"").append(edge.from())
                        .append("\" -> \"").append(edge.to())
                        .append("\" [weight=\"").append(edge.weight()).append("\"];\n");
            }
        }

        for (V vertex : map.getKeys()) {
            if (map.get(vertex).isEmpty()) {
                builder.append("    \"").append(vertex).append("\";\n");
            }
        }

        builder.append("}\n");
        return builder.toString();
    }

    public void addVertex(V value) {
        if (!map.contains(value)) {
            map.add(value, new MyArrayList<DirectedEdge<V>>());
        }
    }

    /**
     * Add an edge between two nodes (with given values) with the given weight.
     * If a node based on fromValue or toValue does not exist, it is automatically added to the graph.
     * <br/><i>Edges are always directed (and always have a weight) i.e. are NOT bidirectional</i>,
     * so adding an edge means that there will only be an edge between from en to node, NOT vice versa.
     *
     * @param fromValue originating node value
     * @param toValue   connected node value
     * @param weight    weight of the edge
     * @throws KeyNotFoundException if fromValue or toValue are not already part of this graph
     */
    @Override
    public void addEdge(V fromValue, V toValue, double weight) throws KeyNotFoundException {

        if (!map.contains(fromValue) || !map.contains(toValue)) {
            throw new KeyNotFoundException("One or both vertices not found in the graph");
        }

        map.get(fromValue).addLast(new DirectedEdge<>(fromValue, toValue, weight));
    }

    /**
     * Add two edges (see addEdge()), effectively connecting from and to in both directions.
     *
     * @param fromValue
     * @param toValue
     * @param weight
     */
    @Override
    public void addEdgeBidirectional(V fromValue, V toValue, double weight) {
        addVertex(fromValue);
        addVertex(toValue);
        addEdge(fromValue, toValue, weight);
        addEdge(toValue, fromValue, weight);
    }

    /**
     * Gets a list of edges from the given node.
     *
     * @param value the value of the node the edges originate from
     * @return a list of edges which originate from the node with the given value
     */
    @Override
    public SaxList<DirectedEdge<V>> getEdges(V value) {
        if (!map.contains(value)) {
            throw new KeyNotFoundException("Vertex not found: " + value);
        }
        return map.get(value);
    }

    /**
     * @return the total weight of (ALL) edges of the graph
     */
    @Override
    public double getTotalWeight() {
        double totalWeight = 0;
        for (V vertex : map.getKeys()) {
            for (DirectedEdge<V> edge : map.get(vertex)) {
                totalWeight += edge.weight();
            }
        }
        return totalWeight;
    }

    /**
     * Execute the Dijkstra algorithm; determine the shortest paths from the start node to all other nodes.
     *
     * @param startNode the node to start searching from
     * @return the graph (a tree!) which only contains the edges which comprise all shortest paths (a copy)
     */
    @Override
    public SaxGraph<V> shortestPathsDijkstra(V startNode) {
        MyGraph<V> result = new MyGraph<>();
        MyHashMap<V, Boolean> visited = new MyHashMap<>();
        MyHashMap<V, Double> distances = new MyHashMap<>();
        MyHeap<DirectedEdge<V>> queue = new MyHeap<>();
        distances.add(startNode, 0.0);
        queue.enqueue(new DirectedEdge<>(startNode, startNode, 0));

        while (!queue.isEmpty()) {
            DirectedEdge<V> edge = queue.dequeue();
            V currentVertex = edge.to();
            if (visited.contains(currentVertex)) {
                continue;
            }
            visited.add(currentVertex, true);

            result.addVertex(currentVertex);
            result.addVertex(edge.from());

            if (!edge.from().equals(currentVertex)) {
                result.addEdge(edge.from(), currentVertex, edge.weight());
            }

            for (DirectedEdge<V> neighborEdge : map.get(currentVertex)) {
                V neighborNode = neighborEdge.to();
                double newDistance = distances.get(currentVertex) + neighborEdge.weight();
                if (!distances.contains(neighborNode) || newDistance < distances.get(neighborNode)) {
                    distances.put(neighborNode, newDistance);
                    queue.enqueue(new DirectedEdge<>(currentVertex, neighborNode, newDistance));
                }
            }
        }
        return result;
    }


    /**
     * Execute the A* algorithm to determine the shortest path from startNode to endNode.
     *
     * @param startNode the node to start searching
     * @param endNode   the target node
     * @param estimator a (handler) function to estimate the distance (weight) between two nodes
     * @return a list of edges (from start to end) which comprise the shortest path from startNode to endNode.
     */
    @Override
    public SaxList<DirectedEdge<V>> shortestPathAStar(V startNode, V endNode, Estimator<V> estimator) {
        MyHeap<AStarNode> openList = new MyHeap<>();
        MyHashMap<V, AStarNode> closedList = new MyHashMap<>();
        AStarNode startAStarNode = new AStarNode(null, 0, estimator.estimate(startNode, endNode));
        openList.enqueue(startAStarNode);

        while (!openList.isEmpty()) {
            AStarNode currentNode = openList.dequeue();
            V currentVertex = (currentNode.directedEdge == null) ? startNode : currentNode.directedEdge.to();

            if (currentVertex.equals(endNode)) {
                closedList.add(currentVertex, currentNode);
                return reconstructPath(closedList, currentNode);
            }

            if (!closedList.contains(currentVertex)) {
                closedList.add(currentVertex, currentNode);
                for (DirectedEdge<V> neighborEdge : map.get(currentVertex)) {
                    V neighborNode = neighborEdge.to();
                    if (closedList.contains(neighborNode)) {
                        continue;
                    }

                    double neighborG = currentNode.g + neighborEdge.weight();
                    double neighborH = estimator.estimate(neighborNode, endNode);
                    AStarNode neighborAStarNode = new AStarNode(neighborEdge, neighborG, neighborH);

                    openList.enqueue(neighborAStarNode);
                }
            }
        }
        return null;
    }

    private SaxList<DirectedEdge<V>> reconstructPath(MyHashMap<V, AStarNode> closedList, AStarNode goalNode) {
        MyArrayList<DirectedEdge<V>> path = new MyArrayList<>();
        AStarNode current = goalNode;

        while (current.directedEdge != null) {
            path.addFirst(current.directedEdge);
            current = closedList.get(current.directedEdge.from());
        }

        return path;
    }


    /**
     * Determine the minimal cost (total weight) of edges which are necessary to connect all nodes.
     * A disconnected graph will still be disconnected, but all edges will be examined;
     * the algorithm must therefore be run on each sub graph.
     *
     * @return the MCST graph (a copy)
     */
    @Override
    public SaxGraph<V> minimumCostSpanningTree() {
        MyGraph<V> mst = new MyGraph<>();
        MyHeap<DirectedEdge<V>> edgeHeap = new MyHeap<>();
        MyHashMap<V, Boolean> visitedNodes = new MyHashMap<>();

        V startNode = getFirstNode();
        if (startNode == null) return mst;

        addNodeToMST(startNode, mst, visitedNodes);
        addEdgesToHeap(startNode, edgeHeap);

        while (!edgeHeap.isEmpty()) {
            DirectedEdge<V> edge = edgeHeap.dequeue();
            V targetNode = edge.to();

            if (visitedNodes.contains(targetNode)) continue;

            addEdgeToMST(edge, mst, visitedNodes);
            addEdgesToHeap(targetNode, edgeHeap);
        }
        return mst;
    }

    private V getFirstNode() {
        Iterator<V> nodeIterator = map.getKeys().iterator();
        return nodeIterator.hasNext() ? nodeIterator.next() : null;
    }

    private void addNodeToMST(V node, MyGraph<V> mst, MyHashMap<V, Boolean> visitedNodes) {
        mst.addVertex(node);
        visitedNodes.add(node, true);
    }

    private void addEdgesToHeap(V node, MyHeap<DirectedEdge<V>> edgeHeap) {
        for (DirectedEdge<V> edge : map.get(node)) {
            edgeHeap.enqueue(edge);
        }
    }

    private void addEdgeToMST(DirectedEdge<V> edge, MyGraph<V> mst, MyHashMap<V, Boolean> visitedNodes) {
        V from = edge.from();
        V to = edge.to();

        mst.addVertex(from);
        mst.addVertex(to);

        mst.addEdge(from, to, edge.weight());
        addNodeToMST(to, mst, visitedNodes);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<V> iterator() {
        return new DFSIterator();
    }

    private class DFSIterator implements Iterator<V> {
        private final MyHashMap<V, Boolean> visited = new MyHashMap<>();
        private final MyStack<V> stack = new MyStack<>();
        private final Iterator<V> keyIterator = map.getKeys().iterator();

        public DFSIterator() {
            findNextUnvisitedVertex();
        }

        private void findNextUnvisitedVertex() {
            while (stack.isEmpty() && keyIterator.hasNext()) {
                V vertex = keyIterator.next();
                if (!visited.contains(vertex)) {
                    stack.push(vertex);
                    visited.add(vertex, true);
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            V current = stack.pop();

            for (DirectedEdge<V> edge : map.get(current)) {
                V neighbor = edge.to();
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                    visited.add(neighbor, true);
                }
            }
            if (stack.isEmpty()) {
                findNextUnvisitedVertex();
            }
            return current;
        }
    }

    public class AStarNode implements Comparable<AStarNode> {
        double g;
        double h;
        DirectedEdge<V> directedEdge;

        public AStarNode(DirectedEdge<V> edge, double g, double h) {
            this.directedEdge = edge;
            this.g = g;
            this.h = h;
        }

        @Override
        public int compareTo(AStarNode other) {
            return Double.compare(this.g + this.h, other.g + other.h);
        }
    }
}
