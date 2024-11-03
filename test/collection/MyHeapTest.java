package collection;

import nl.saxion.cds.solution.MyHeap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import nl.saxion.cds.collection.EmptyCollectionException;

import static org.junit.jupiter.api.Assertions.*;
public class MyHeapTest {

    private MyHeap<Integer> heap;

    @BeforeEach
    void setUp() {
        heap = new MyHeap<>();
    }

    @Test
    void GivenEmptyHeap_WhenCheckingIsEmpty_ThenReturnTrue() {
        assertTrue(heap.isEmpty(), "Expected heap to be empty");
    }

    @Test
    void GivenNonEmptyHeap_WhenCheckingIsEmpty_ThenReturnFalse() {
        heap.enqueue(1);
        assertFalse(heap.isEmpty(), "Expected heap to not be empty");
    }

    @Test
    void GivenEmptyHeap_WhenGettingSize_ThenReturnZero() {
        assertEquals(0, heap.size(), "Expected size to be zero");
    }

    @Test
    void GivenNonEmptyHeap_WhenGettingSize_ThenReturnCorrectSize() {
        heap.enqueue(1);
        heap.enqueue(2);
        assertEquals(2, heap.size(), "Expected size to be two");
    }

    @Test
    void GivenEmptyHeap_WhenPeeking_ThenThrowEmptyCollectionException() {
        assertThrows(EmptyCollectionException.class, heap::peek, "Expected EmptyCollectionException when peeking empty heap");
    }

    @Test
    void GivenNonEmptyHeap_WhenPeeking_ThenReturnRootElement() {
        heap.enqueue(10);
        heap.enqueue(5);
        heap.enqueue(15);
        assertEquals(5, heap.peek(), "Expected root element to be the smallest value in the heap");
    }

    @Test
    void GivenEmptyHeap_WhenDequeuing_ThenThrowEmptyCollectionException() {
        assertThrows(EmptyCollectionException.class, heap::dequeue, "Expected EmptyCollectionException when dequeuing empty heap");
    }

    @Test
    void GivenNonEmptyHeap_WhenDequeuing_ThenReturnRootElementAndPreserveHeapOrder() throws EmptyCollectionException {
        heap.enqueue(10);
        heap.enqueue(5);
        heap.enqueue(15);
        heap.enqueue(3);

        assertEquals(3, heap.dequeue(), "Expected root element to be the smallest value in the heap");
        assertEquals(5, heap.dequeue(), "Expected next smallest value after dequeuing root");
        assertEquals(10, heap.dequeue(), "Expected next smallest value after dequeuing root");
        assertEquals(15, heap.dequeue(), "Expected last element to be the largest value in the heap");

        assertTrue(heap.isEmpty(), "Expected heap to be empty after dequeuing all elements");
    }

    @Test
    void GivenHeap_WhenAddingMultipleElements_ThenEnsureHeapProperty() throws EmptyCollectionException {
        heap.enqueue(20);
        heap.enqueue(15);
        heap.enqueue(30);
        heap.enqueue(5);

        assertEquals(5, heap.dequeue(), "Expected smallest element after heap property enforcement");
        assertEquals(15, heap.dequeue(), "Expected next smallest element after heap property enforcement");
        assertEquals(20, heap.dequeue(), "Expected next smallest element after heap property enforcement");
        assertEquals(30, heap.dequeue(), "Expected largest element after heap property enforcement");
    }

    @Test
    void GivenHeap_WhenGeneratingGraphViz_ThenReturnCorrectRepresentation() {
        heap.enqueue(10);
        heap.enqueue(5);
        heap.enqueue(15);

        String expectedGraph = "digraph test {\n" +
                "    node [shape=circle];\n" +
                "    \"5\";\n" +
                "    \"10\";\n" +
                "    \"15\";\n" +
                "    \"5\" -> \"10\";\n" +
                "    \"5\" -> \"15\";\n" +
                "}\n";
        assertEquals(expectedGraph, heap.graphViz("test"), "Expected correct GraphViz representation for heap structure");
    }

}