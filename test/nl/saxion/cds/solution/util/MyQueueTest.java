package nl.saxion.cds.solution.util;


import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.solution.util.MyQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MyQueueTest {

    private MyQueue<String> queue;

    @BeforeEach
    void setUp() {
        queue = new MyQueue<>();
    }

    @Test
    void GivenNewQueue_WhenCheckingIfEmpty_ThenReturnsTrue() {
        assertTrue(queue.isEmpty(), "Queue should be empty after initialization");
    }

    @Test
    void GivenQueueWithElements_WhenCheckingIfEmpty_ThenReturnsFalse() {
        queue.enqueue("first");
        assertFalse(queue.isEmpty(), "Queue should not be empty after adding elements");
    }

    @Test
    void GivenNewQueue_WhenCheckingSize_ThenReturnsZero() {
        assertEquals(0, queue.size(), "Queue size should be zero after initialization");
    }

    @Test
    void GivenQueueWithElements_WhenCheckingSize_ThenReturnsCorrectSize() {
        queue.enqueue("first");
        queue.enqueue("second");
        assertEquals(2, queue.size(), "Queue size should be equal to the number of added elements");
    }

    @Test
    void GivenQueueWithOneElement_WhenDequeuing_ThenReturnsElementAndQueueIsEmpty() throws EmptyCollectionException {
        queue.enqueue("first");
        String dequeuedElement = queue.dequeue();
        assertEquals("first", dequeuedElement, "Dequeued element should match the enqueued element");
        assertTrue(queue.isEmpty(), "Queue should be empty after dequeuing the only element");
    }

    @Test
    void GivenQueueWithMultipleElements_WhenDequeuing_ThenReturnsElementsInCorrectOrder() throws EmptyCollectionException {
        queue.enqueue("first");
        queue.enqueue("second");
        String firstDequeued = queue.dequeue();
        String secondDequeued = queue.dequeue();

        assertEquals("first", firstDequeued, "First dequeued element should match the first enqueued element");
        assertEquals("second", secondDequeued, "Second dequeued element should match the second enqueued element");
        assertTrue(queue.isEmpty(), "Queue should be empty after dequeuing all elements");
    }

    @Test
    void GivenNewQueue_WhenDequeuing_ThenThrowsEmptyCollectionException() {
        assertThrows(EmptyCollectionException.class, queue::dequeue, "Dequeuing from an empty queue should throw an exception");
    }

    @Test
    void GivenQueueWithElements_WhenPeeking_ThenReturnsFirstElementWithoutRemovingIt() throws EmptyCollectionException {
        queue.enqueue("first");
        queue.enqueue("second");

        String peekedElement = queue.peek();
        assertEquals("first", peekedElement, "Peeked element should match the first enqueued element");
        assertEquals(2, queue.size(), "Queue size should not change after peeking");
    }

    @Test
    void GivenNewQueue_WhenPeeking_ThenThrowsEmptyCollectionException() {
        assertThrows(EmptyCollectionException.class, queue::peek, "Peeking from an empty queue should throw an exception");
    }

    @Test
    void GivenQueueWithElements_WhenCallingGraphViz_ThenReturnsGraphRepresentation() {
        queue.enqueue("first");
        queue.enqueue("second");

        String graphVizOutput = queue.graphViz("TestGraph");
        assertNotNull(graphVizOutput, "GraphViz output should not be null");
        assertTrue(graphVizOutput.contains("TestGraph"), "GraphViz output should contain the name of the graph");
    }
}

