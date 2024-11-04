package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxSearchable;
import nl.saxion.cds.collection.ValueNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class TestMyArrayList {
    // Make sure a lot of resizing has to be done
    private static final int BIG_NUMBER_OF_ELEMENTS = 5000;
    private MyArrayList<String> list;

    @BeforeEach
    void createExampleList() {
        list = new MyArrayList<>();
        list.addLast("2");
        list.addLast("23");
        list.addLast("a");
        list.addLast("dd");
        list.addLast("7a");
    }


    @Test
    void GivenEmptyList_WhenCallingGetters_ConfirmListIsActuallyEmpty() {
        MyArrayList<Object> myArrayList = new MyArrayList<>();
        assertTrue(myArrayList.isEmpty());
        assertEquals(0, myArrayList.size());
        assertEquals("[ ]", myArrayList.toString());
    }

    @Test
    void GivenEmptyList_WhenAccessingAnyIndex_ThenIndexOutOfBoundsThrown() {
        MyArrayList<Object> myEmptyList = new MyArrayList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.addAt(-1, 666));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.addAt(1, 666));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.set(-1, 666));
        assertThrows(IndexOutOfBoundsException.class, () -> myEmptyList.set(1, 666));
        assertThrows(EmptyCollectionException.class, myEmptyList::removeFirst);
        assertThrows(EmptyCollectionException.class, myEmptyList::removeLast);
    }

    @Test
    void GivenSheetsList_WhenNoChanges_ConfirmInitialContent() {
        assertEquals(5, list.size());
        assertFalse(list.isEmpty());
        assertEquals("[ 2 23 a dd 7a ]", list.toString());

        // Testing GraphViz can best be done manually (copy past to https://dreampuf.github.io/GraphvizOnline)
        System.out.println(list.graphViz());
    }

    @Test
    void GivenSheetsList_WhenCallingContains_ConfirmCorrectResponses() {
        // Test edge cases in contains()
        assertTrue(list.contains("2"));
        assertTrue(list.contains("7a"));
        assertFalse(list.contains("huh?"));
    }

    @Test
    void GivenSheetsList_WhenAddingAtBeginning_ConfirmChangesAreCorrect() {
        // Insert at front
        list.addFirst("b3");
        assertEquals(6, list.size());
        assertFalse(list.isEmpty());
        assertEquals("[ b3 2 23 a dd 7a ]", list.toString());

        assertThrows(ValueNotFoundException.class, () -> list.remove("huh?"));
    }

    @Test
    void GivenSheetsList_WhenAddingAtIndex_ConfirmChangesAreCorrect() {
        // Insert before
        list.addAt(4, "b3");
        assertEquals(6, list.size());
        assertFalse(list.isEmpty());
        assertEquals("[ 2 23 a dd b3 7a ]", list.toString());
    }

    @Test
    void GivenSheetsList_WhenRemovingElement_ConfirmChangesAreCorrect() {
        // Remove specific element
        list.remove("dd");
        assertEquals("[ 2 23 a 7a ]", list.toString());
        assertEquals(4, list.size());
        assertFalse(list.isEmpty());
    }

    @Test
    void GivenSheetsList_WhenRemovingAllElement_ConfirmChangesAreCorrect() {
        // Edge cases remove
        list.remove("7a");
        list.remove("2");
        assertEquals("[ 23 a dd ]", list.toString());
        assertEquals(3, list.size());
        // Further empty the list.
        assertEquals("23", list.removeFirst());
        assertEquals("dd", list.removeLast());
        assertEquals("a", list.removeFirst());
        // Confirm emtpyness of the list.
        assertEquals("[ ]", list.toString());
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        assertThrows(EmptyCollectionException.class, list::removeFirst);
        assertThrows(EmptyCollectionException.class, list::removeLast);
    }

    @Test
    void GivenSheetsList_WhenAccessingIndexOutOfBounds_ThenExceptionIsThrown() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(list.size()));
    }

    @Test
    void GivenSheetsList_WhenAddingNullValues_ThenListContainsNullValues() {
        list.addAt(0, null); // check addAt with index equals first element
        list.addAt(5, null); // check addAt with index equals last element
        list.addAt(7, null); // check addAt with index just after last element
        assertEquals("[ null 2 23 a dd null 7a null ]", list.toString());
        assertEquals(8, list.size());
        assertFalse(list.isSorted(String::compareTo));

        // Testing GraphViz can best be done manually (copy past to https://dreampuf.github.io/GraphvizOnline)
        System.out.println(list.graphViz());

        // Remove specific element
        list.remove("dd");
        list.remove(null);
        list.remove(null);
        list.remove(null);
        // Remove element using the iterator.
        var iterator = list.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            if (element.compareTo("a") == 0) {
                iterator.remove();
            }
        }
        assertEquals(3, list.size());
        assertFalse(list.isEmpty());
        assertEquals("[ 2 23 7a ]", list.toString());

        // Confirm exception is thrown when no more null values are present.
        assertThrows(ValueNotFoundException.class, () -> list.remove(null));
    }

    @Test
    void GivenListWithIntegers_WhenQuicksorted_ThenListIsSorted() {
        MyArrayList<Integer> list3 = createIntegerArrayList();
        list3.quickSort(Integer::compareTo);
        System.out.println(list3);
        assertTrue(list3.isSorted(Integer::compareTo));
        // Loop through the list and confirm each value is larger than the previous
        int last = -100;
        for (int current : list3) {
            assertTrue(current >= last);
            last = current;
        }
    }

    MyArrayList<Integer> createIntegerArrayList() {
        MyArrayList<Integer> list = new MyArrayList<>();
        list.addLast(8);
        list.addLast(1);
        list.addLast(5);
        list.addLast(14);
        list.addLast(4);
        list.addLast(15);
        list.addLast(12);
        list.addLast(6);
        list.addLast(2);
        list.addLast(11);
        list.addLast(10);
        list.addLast(7);
        list.addLast(9);
        return list;
    }

    @Test
    void GivenLargeList_WhenMakingChanges_ConfirmStateRemainsCorrect() {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < BIG_NUMBER_OF_ELEMENTS; ++i) {
            list.addLast(i);
        }
        assertEquals(BIG_NUMBER_OF_ELEMENTS, list.size());

        // Test removing all elements one by one
        assertEquals(BIG_NUMBER_OF_ELEMENTS, list.size());
        for (int i = BIG_NUMBER_OF_ELEMENTS / 2; i > 0; --i) {
            assertEquals(i, list.removeAt(i));
            list.removeLast();
        }
        assertFalse(list.contains(0));
        assertEquals(0, list.size());

        // Create a list of random integers to test with simpleSort()
        MyArrayList<Integer> list2 = new MyArrayList<>();
        var random = new Random();
        for (int i = 0; i < BIG_NUMBER_OF_ELEMENTS; ++i) {
            list2.addLast(random.nextInt(0, BIG_NUMBER_OF_ELEMENTS));
        }
        assertEquals(BIG_NUMBER_OF_ELEMENTS, list2.size());
        assertFalse(list2.isSorted(Integer::compareTo));
        list2.simpleSort(Integer::compareTo);
        assertTrue(list2.isSorted(Integer::compareTo));

        // Create a list of random integers to test with quickSort()
        MyArrayList<Integer> list3 = new MyArrayList<>();
        for (int i = 0; i < BIG_NUMBER_OF_ELEMENTS; ++i) {
            list3.addLast(random.nextInt(0, BIG_NUMBER_OF_ELEMENTS));
        }
        assertEquals(BIG_NUMBER_OF_ELEMENTS, list3.size());
        assertFalse(list3.isSorted(Integer::compareTo));
        list3.quickSort(Integer::compareTo);
        assertTrue(list3.isSorted(Integer::compareTo));

        // Test BinarySearch
        int v = list3.get(0);
        int i = list3.binarySearch(Integer::compareTo, v);
        assertEquals(v, list3.get(i)); // Compare value, because list may contain double values
        v = list3.get(BIG_NUMBER_OF_ELEMENTS - 1);
        i = list3.binarySearch(Integer::compareTo, v);
        assertEquals(v, list3.get(i));
        v = list3.get(BIG_NUMBER_OF_ELEMENTS - 2);
        i = list3.binarySearch(Integer::compareTo, v);
        assertEquals(v, list3.get(i));
        assertEquals(SaxSearchable.NOT_FOUND, list3.binarySearch(Integer::compareTo, -1));

        // Test linearSearch
        v = list3.get(0);
        i = list3.linearSearch(v); // Compare value, because list may contain double values
        assertEquals(v, list3.get(i));
        v = list3.get(BIG_NUMBER_OF_ELEMENTS - 1);
        i = list3.linearSearch(v);
        assertEquals(v, list3.get(i));
        v = list3.get(BIG_NUMBER_OF_ELEMENTS - 2);
        i = list3.linearSearch(v);
        assertEquals(v, list3.get(i));
        assertEquals(SaxSearchable.NOT_FOUND, list3.linearSearch(-1));
    }
}
