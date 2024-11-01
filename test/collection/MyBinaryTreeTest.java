package collection;

import nl.saxion.cds.collection.DuplicateKeyException;
import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.solution.MyBinaryTree;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyBinaryTreeTest {
    private MyBinaryTree<Integer, String> binaryTree;

    @Before
    public void setUp() {
        binaryTree = new MyBinaryTree<>();
    }

    @Test
    public void testAddAndContains() throws DuplicateKeyException {
        binaryTree.add(50, "One");
        binaryTree.add(25, "Five");
        binaryTree.add(100, "Five");
        binaryTree.add(75, "Five");
        System.out.println(binaryTree.graphViz());

        assertTrue(binaryTree.contains(50));
        assertFalse(binaryTree.contains(10));
    }

    @Test
    public void testGet() throws DuplicateKeyException {
        binaryTree.add(5, "Five");
        assertEquals("Five", binaryTree.get(5));
        assertNull(binaryTree.get(10));
    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddDuplicateKey() throws DuplicateKeyException {
        binaryTree.add(5, "Five");
        binaryTree.add(5, "Duplicate");
    }

    @Test
    public void testRemoveKeyNotFound() {
        try {
            binaryTree.remove(10);
            fail("Expected KeyNotFoundException");
        } catch (KeyNotFoundException e) {
            assertEquals("Key \"10\" is not found.", e.getMessage());
        }
    }

    @Test
    public void testSize() throws DuplicateKeyException {
        assertEquals(0, binaryTree.size());
        binaryTree.add(5, "Five");
        assertEquals(1, binaryTree.size());
        binaryTree.add(3, "Three");
        assertEquals(2, binaryTree.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(binaryTree.isEmpty());
        binaryTree.add(5, "Five");
        assertFalse(binaryTree.isEmpty());
    }

    // Добавьте дополнительные тесты для других методов, таких как getKeys() и graphViz()
}
