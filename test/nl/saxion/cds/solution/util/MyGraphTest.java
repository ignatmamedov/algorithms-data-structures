package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxGraph;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.solution.util.MyGraph;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MyGraphTest<V> {

    private MyGraph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new MyGraph<>();
    }

    @Test
    void GivenEmptyGraph_WhenCheckingIsEmpty_ConfirmTrue() {
        assertTrue(graph.isEmpty());
    }

    @Test
    void GivenEmptyGraph_WhenCheckingSize_ConfirmZero() {
        assertEquals(0, graph.size());
    }

    @Test
    void GivenGraphWithVertices_WhenAddingEdges_ConfirmCorrectEdges() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 2.5);
        graph.addEdge("A", "C", 3.5);
        assertEquals(3, graph.size());
        assertEquals(2, graph.getEdges("A").size());
        assertEquals(2.5, graph.getEdges("A").get(0).weight());
    }

    @Test
    void GivenGraphWithVertices_WhenAddingEdge_ConfirmCorrectWeightAndDirection() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", 5.0);
        assertEquals(5.0, graph.getEdges("A").get(0).weight());
        assertEquals("B", graph.getEdges("A").get(0).to());
    }

    @Test
    void GivenGraphWithVertices_WhenAddingBidirectionalEdge_ConfirmBothDirectionsExist() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdgeBidirectional("A", "B", 7.5);
        assertEquals(7.5, graph.getEdges("A").get(0).weight());
        assertEquals(7.5, graph.getEdges("B").get(0).weight());
    }

    @Test
    void GivenNonExistentNode_WhenGettingEdges_ExpectKeyNotFoundException() {
        assertThrows(KeyNotFoundException.class, () -> graph.getEdges("NonExistentNode"));
    }

    @Test
    void GivenGraphWithVertices_WhenAddingEdges_ConfirmTotalWeight() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 3.0);
        graph.addEdge("B", "C", 4.0);
        assertEquals(7.0, graph.getTotalWeight());
    }

    @Test
    void GivenGraphWithMultipleNodes_WhenUsingDijkstraAlgorithm_ConfirmShortestPath() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 4);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 1);

        SaxGraph<String> result = graph.shortestPathsDijkstra("A");

        assertNotNull(result);
        assertEquals(1, result.getEdges("A").size());
        assertEquals(4, result.size());
    }

    @Test
    void GivenGraphWithPath_WhenUsingAStarAlgorithm_ConfirmShortestPath() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 2);
        graph.addEdge("A", "C", 4);
        SaxGraph.Estimator<String> estimator = (from, to) -> 0.0;

        SaxList<SaxGraph.DirectedEdge<String>> path = graph.shortestPathAStar("A", "C", estimator);

        assertNotNull(path);
        assertEquals(2, path.size());
    }

    @Test
    void GivenGraphWithConnectedComponents_WhenUsingMinimumSpanningTree_ConfirmMSTWithAllNodes() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("H");
        graph.addVertex("G");
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 3);
        graph.addEdge("D", "E", 4);
        graph.addEdge("A", "E", 5);
        graph.addEdge("H", "G", 5);

        SaxGraph<String> mst = graph.minimumCostSpanningTree();
        assertNotNull(mst);
        assertEquals(5, mst.size());
    }

    @Test
    void GivenGraphWithMultipleNodes_WhenUsingDFSIterator_ConfirmTraversalOrder() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 2);
        graph.addEdge("B", "D", 1);
        graph.addEdge("C", "E", 2);

        Iterator<String> dfsIterator = graph.iterator();
        assertTrue(dfsIterator.hasNext());
        assertEquals("A", dfsIterator.next());
    }

    @Test
    void GivenEmptyGraph_WhenUsingIterator_ExpectNoSuchElementException() {
        Iterator<String> iterator = graph.iterator();
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void GivenGraphWithSingleNode_WhenCheckingIsEmpty_ConfirmFalse() {
        graph.addVertex("A");
        assertFalse(graph.isEmpty());
    }

    @Test
    void GivenGraphWithSingleNode_WhenCheckingSize_ConfirmOne() {
        graph.addVertex("A");
        assertEquals(1, graph.size());
    }

    @Test
    void GivenGraphWithInvalidEdge_WhenAddingEdge_ExpectKeyNotFoundException() {
        assertThrows(KeyNotFoundException.class, () -> graph.addEdge("NonExistentNode", "B", 5.0));
    }

    @Test
    void GivenGraphWithMultipleNodes_WhenCheckingGetTotalWeight_ConfirmCorrectValue() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addEdge("A", "B", 1.5);
        graph.addEdge("B", "C", 2.0);
        graph.addEdge("C", "D", 2.5);
        assertEquals(6.0, graph.getTotalWeight());
    }

    @Test
    void GivenDuplicateVertex_WhenAddingVertex_ConfirmNoDuplicates() {
        graph.addVertex("A");
        graph.addVertex("A");
        assertEquals(1, graph.size());
    }

    @Test
    void GivenGraphWithNonExistentVertices_WhenAddingEdge_ExpectKeyNotFoundException() {
        graph.addVertex("A");
        assertThrows(KeyNotFoundException.class, () -> graph.addEdge("A", "NonExistentNode", 2.0));
        assertThrows(KeyNotFoundException.class, () -> graph.addEdge("NonExistentNode", "A", 2.0));
    }

    @Test
    void GivenDisconnectedNodes_WhenUsingMinimumSpanningTree_ExpectPartialMST() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);

        SaxGraph<String> mst = graph.minimumCostSpanningTree();
        assertNotNull(mst);
        assertTrue(mst.size() <= graph.size());
    }

    @Test
    void GivenUnreachableTargetInAStar_WhenSearchingPath_ExpectNull() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 1.0);
        SaxGraph.Estimator<String> estimator = (from, to) -> 0.0;
        assertNull(graph.shortestPathAStar("A", "C", estimator));
    }

    @Test
    void GivenGraph_WhenUsingGraphViz_ConfirmCorrectFormat() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B", 2.5);
        graph.addEdge("B", "C", 3.5);

        String expectedGraphViz = "digraph MyGraph {\n" +
                "    \"A\" -> \"B\" [weight=\"2.5\"];\n" +
                "    \"B\" -> \"C\" [weight=\"3.5\"];\n" +
                "    \"C\";\n" +
                "}\n";
        assertEquals(expectedGraphViz, graph.graphViz("MyGraph"));
    }

    @Test
    void GivenGraphWithNoEdges_WhenUsingGraphViz_ConfirmVerticesOnly() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        String expectedGraphViz = "digraph TestGraph {\n" +
                "    \"A\";\n" +
                "    \"B\";\n" +
                "    \"C\";\n" +
                "}\n";
        assertEquals(expectedGraphViz, graph.graphViz("TestGraph"));
    }

    @Test
    void GivenSelfLoop_WhenAddingEdge_ConfirmGraphVizOutput() {
        graph.addVertex("A");
        graph.addEdge("A", "A", 1.0);

        String expectedGraphViz = "digraph SelfLoopGraph {\n" +
                "    \"A\" -> \"A\" [weight=\"1.0\"];\n" +
                "}\n";
        assertEquals(expectedGraphViz, graph.graphViz("SelfLoopGraph"));
    }
}

