package collection;

import nl.saxion.cds.collection.DuplicateKeyException;
import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.solution.MyBinaryTree;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public
class MyBinaryTreeTest {

    private MyBinaryTree<Integer, String> tree;

    @BeforeEach
    void setUp() {
        tree = new MyBinaryTree<>();
    }

    @Test
    public void GivenEmptyTree_WhenCheckingIsEmpty_ConfirmTrue() {
        assertTrue(tree.isEmpty());
        assertEquals(0, tree.size());
    }

    @Test
    public void GivenTreeWithElements_WhenCheckingSizeAndIsEmpty_ConfirmCorrectSize() {
        tree.add(1, "One");
        assertFalse(tree.isEmpty());
        assertEquals(1, tree.size());
    }

    @Test
    public void GivenTree_WhenAddingDuplicateKey_ExpectDuplicateKeyException() {
        tree.add(1, "One");
        assertThrows(DuplicateKeyException.class, () -> tree.add(1, "Duplicate"));
    }

    @Test
    public void GivenTree_WhenAddingElements_ConfirmContainsAndGetCorrectValues() {
        tree.add(1, "One");
        tree.add(2, "Two");
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(2));
        assertFalse(tree.contains(3));
        assertEquals("One", tree.get(1));
        assertEquals("Two", tree.get(2));
        assertNull(tree.get(3));
    }

    @Test
    public void GivenTree_WhenRemovingNonExistentKey_ExpectKeyNotFoundException() {
        assertThrows(KeyNotFoundException.class, () -> tree.remove(1));
    }

    @Test
    public void GivenTree_WhenRemovingElement_ConfirmSizeAndContainsUpdated() {
        tree.add(1, "One");
        tree.add(2, "Two");
        assertEquals("One", tree.remove(1));
        assertFalse(tree.contains(1));
        assertEquals(1, tree.size());
    }

    @Test
    public void GivenTree_WhenRemovingRoot_ConfirmStructureAndSize() {
        tree.add(2, "Two");
        tree.add(1, "One");
        tree.add(3, "Three");
        assertEquals("Two", tree.remove(2));
        assertEquals(2, tree.size());
        assertFalse(tree.contains(2));
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(3));
    }

    @Test
    public void GivenTreeWithLeftHeavyUnbalanced_WhenAdding_ConfirmBalancedStructure() {
        tree.add(3, "Three");
        tree.add(2, "Two");
        tree.add(1, "One");
        assertEquals(3, tree.size());
        assertEquals("Two", tree.get(2));
    }

    @Test
    public void GivenTreeWithRightHeavyUnbalanced_WhenAdding_ConfirmBalancedStructure() {
        tree.add(1, "One");
        tree.add(2, "Two");
        tree.add(3, "Three");
        assertEquals(3, tree.size());
        assertEquals("Two", tree.get(2));
    }

    @Test
    public void GivenTreeWithComplexStructure_WhenRemovingNodeWithTwoChildren_ConfirmStructure() {
        tree.add(5, "Five");
        tree.add(3, "Three");
        tree.add(7, "Seven");
        tree.add(2, "Two");
        tree.add(4, "Four");
        tree.add(6, "Six");
        tree.add(8, "Eight");

        assertEquals("Five", tree.remove(5));
        assertEquals(6, tree.size());
        assertTrue(tree.contains(6));
        assertFalse(tree.contains(5));
    }

    @Test
    public void GivenTree_WhenUsingGetKeys_ConfirmCorrectOrder() {
        tree.add(2, "Two");
        tree.add(1, "One");
        tree.add(3, "Three");

        SaxList<Integer> keys = tree.getKeys();
        assertEquals(3, keys.size());
        assertEquals(1, (int)keys.get(0));
        assertEquals(2, (int)keys.get(1));
        assertEquals(3, (int)keys.get(2));
    }

    @Test
    public void GivenTree_WhenUsingGraphViz_ConfirmCorrectFormat() {
        tree.add(2, "Two");
        tree.add(1, "One");
        tree.add(3, "Three");

        String expectedGraphViz = "digraph MyBinaryTree {\n" +
                "    node [shape=circle];\n" +
                "    2 [label=\"Two\"];\n" +
                "    2 -> 1;\n" +
                "    1 [label=\"One\"];\n" +
                "    2 -> 3;\n" +
                "    3 [label=\"Three\"];\n" +
                "}\n";
        assertEquals(expectedGraphViz, tree.graphViz("MyBinaryTree"));
    }

    @Test
    public void GivenTree_WhenRemovingSingleChildNode_ConfirmCorrectStructure() {
        tree.add(1, "One");
        tree.add(2, "Two");
        assertEquals("One", tree.remove(1));
        assertEquals(1, tree.size());
        assertTrue(tree.contains(2));
        assertFalse(tree.contains(1));
    }

    @Test
    public void GivenTree_WhenRemovingLeafNode_ConfirmCorrectStructure() {
        tree.add(1, "One");
        tree.add(2, "Two");
        tree.add(3, "Three");
        assertEquals("Three", tree.remove(3));
        assertEquals(2, tree.size());
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(2));
        assertFalse(tree.contains(3));
    }

    @Test
    public void GivenTreeWithDuplicateRemoveOperation_WhenCheckingSizeAndContains_ConfirmCorrect() {
        tree.add(1, "One");
        tree.add(2, "Two");
        assertEquals("One", tree.remove(1));
        assertThrows(KeyNotFoundException.class, () -> tree.remove(1));
        assertEquals(1, tree.size());
    }
}
