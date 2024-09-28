package collection;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.ValueNotFoundException;
import nl.saxion.cds.solution.DLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DLinkedListTest {

    private DLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new DLinkedList<>();
    }

    @Test
    void GivenEmptyList_WhenAddingElements_ConfirmCorrectSizeAndContent() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void GivenEmptyList_WhenAddingFirstElement_ConfirmCorrectSizeAndContent() {
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void GivenListWithElements_WhenAddingAtIndex_ConfirmCorrectSizeAndContent() {
        list.addLast(1);
        list.addLast(3);
        list.addAt(1, 2);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    void GivenListWithElements_WhenSettingElementAtIndex_ConfirmValueIsUpdated() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.set(1, 4);
        assertEquals(4, list.get(1));
    }

    @Test
    void GivenListWithElements_WhenCheckingContains_ConfirmCorrectResponses() {
        list.addLast(1);
        list.addLast(2);
        assertTrue(list.contains(1));
        assertFalse(list.contains(3));
    }

    @Test
    void GivenListWithElements_WhenRemovingFirstElement_ConfirmCorrectSizeAndContent() throws EmptyCollectionException {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(1, list.removeFirst());
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
    }

    @Test
    void GivenListWithElements_WhenRemovingLastElement_ConfirmCorrectSizeAndContent() throws EmptyCollectionException {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(3, list.removeLast());
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    void GivenListWithElements_WhenRemovingElementAtIndex_ConfirmCorrectSizeAndContent() throws EmptyCollectionException {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(2, list.removeAt(1));
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test
    void GivenListWithElements_WhenRemovingSpecificValue_ConfirmCorrectSizeAndContent() throws ValueNotFoundException {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        list.remove((Integer) 2);
        assertEquals(2, list.size());
        assertFalse(list.contains(2));
    }

    @Test
    void GivenListWithElements_WhenRemovingValueNotFound_ThenExceptionIsThrown() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertThrows(ValueNotFoundException.class, () -> list.remove(4));
    }

    @Test
    void GivenEmptyList_WhenRemovingFirst_ThenExceptionIsThrown() {
        assertThrows(EmptyCollectionException.class, () -> list.removeFirst());
    }

    @Test
    void GivenEmptyList_WhenRemovingLast_ThenExceptionIsThrown() {
        assertThrows(EmptyCollectionException.class, () -> list.removeLast());
    }

    @Test
    void GivenListWithElements_WhenAccessingInvalidIndex_ThenExceptionIsThrown() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));
    }

    @Test
    void GivenListWithElements_WhenSettingInvalidIndex_ThenExceptionIsThrown() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(2, 2));
    }

    @Test
    void GivenListWithElements_WhenAddingAtInvalidIndex_ThenExceptionIsThrown() {
        list.addLast(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.addAt(3, 2));
    }

    @Test
    void GivenEmptyList_WhenCheckingIsEmpty_ConfirmCorrectResponse() {
        assertTrue(list.isEmpty());
        list.addLast(1);
        assertFalse(list.isEmpty());
    }

    @Test
    void GivenEmptyList_WhenCheckingSize_ConfirmCorrectSize() {
        assertEquals(0, list.size());
        list.addLast(1);
        assertEquals(1, list.size());
    }

    @Test
    void GivenListWithElements_WhenVisualizingGraph_ConfirmCorrectOutput() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        String expectedGraphViz = "digraph list {\n" +
                "\"1\" -> \"2\"\n" +
                "\"2\" -> \"3\"\n" +
                "}";
        assertEquals(expectedGraphViz, list.graphViz("list"));
    }
}
