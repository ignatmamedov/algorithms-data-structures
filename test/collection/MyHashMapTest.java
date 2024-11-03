package collection;

import nl.saxion.cds.collection.DuplicateKeyException;
import nl.saxion.cds.collection.KeyNotFoundException;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.solution.MyHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTest {

    private MyHashMap<String, Integer> hashMap;

    @BeforeEach
    void setUp() {
        hashMap = new MyHashMap<>();
    }

    @Test
    void GivenEmptyHashMap_WhenCheckingIsEmpty_ThenReturnTrue() {
        assertTrue(hashMap.isEmpty(), "Expected hash map to be empty");
    }

    @Test
    void GivenNonEmptyHashMap_WhenCheckingIsEmpty_ThenReturnFalse() {
        hashMap.add("Key1", 1);
        assertFalse(hashMap.isEmpty(), "Expected hash map to not be empty");
    }

    @Test
    void GivenEmptyHashMap_WhenGettingSize_ThenReturnZero() {
        assertEquals(0, hashMap.size(), "Expected size to be zero");
    }

    @Test
    void GivenNonEmptyHashMap_WhenGettingSize_ThenReturnCorrectSize() {
        hashMap.add("Key1", 1);
        hashMap.add("Key2", 2);
        assertEquals(2, hashMap.size(), "Expected size to be two");
    }

    @Test
    void GivenKeyInHashMap_WhenCheckingContainsKey_ThenReturnTrue() {
        hashMap.add("Key1", 1);
        assertTrue(hashMap.contains("Key1"), "Expected hash map to contain 'Key1'");
    }

    @Test
    void GivenKeyNotInHashMap_WhenCheckingContainsKey_ThenReturnFalse() {
        assertFalse(hashMap.contains("Key1"), "Expected hash map to not contain 'Key1'");
    }

    @Test
    void GivenKeyInHashMap_WhenGettingValue_ThenReturnCorrectValue() {
        hashMap.add("Key1", 1);
        assertEquals(1, hashMap.get("Key1"), "Expected value to be 1 for 'Key1'");
    }

    @Test
    void GivenKeyNotInHashMap_WhenGettingValue_ThenReturnNull() {
        assertNull(hashMap.get("NonExistentKey"), "Expected null value for non-existent key");
    }

    @Test
    void GivenKeyAlreadyInHashMap_WhenAddingDuplicateKey_ThenThrowDuplicateKeyException() {
        hashMap.add("Key1", 1);
        assertThrows(DuplicateKeyException.class, () -> hashMap.add("Key1", 2), "Expected DuplicateKeyException for duplicate key");
    }

    @Test
    void GivenKeyInHashMap_WhenRemovingKey_ThenReturnCorrectValueAndDecreaseSize() throws KeyNotFoundException {
        hashMap.add("Key1", 1);
        assertEquals(1, hashMap.remove("Key1"), "Expected value 1 to be removed for 'Key1'");
        assertEquals(0, hashMap.size(), "Expected size to be zero after removal");
    }

    @Test
    void GivenKeyNotInHashMap_WhenRemovingKey_ThenThrowKeyNotFoundException() {
        assertThrows(KeyNotFoundException.class, () -> hashMap.remove("NonExistentKey"), "Expected KeyNotFoundException for non-existent key");
    }

    @Test
    void GivenEmptyHashMap_WhenGeneratingGraphViz_ThenReturnEmptyGraph() {
        String expectedGraph = "digraph test {\n}";
        assertEquals(expectedGraph, hashMap.graphViz("test"), "Expected empty GraphViz representation");
    }

    @Test
    void GivenNonEmptyHashMap_WhenGeneratingGraphViz_ThenReturnCorrectGraph() {
        hashMap.add("Key1", 1);
        hashMap.add("Key2", 2);
        String graphVizOutput = hashMap.graphViz("test");

        String expectedPattern = "digraph test \\{\\n\\d+: \"Key1=1\";\\n\\d+: \"Key2=2\";\\n}";
        assertTrue(graphVizOutput.matches(expectedPattern), "Expected correct GraphViz representation");
    }

    @Test
    void GivenEmptyHashMap_WhenGettingKeys_ThenReturnEmptyList() {
        SaxList<String> keys = hashMap.getKeys();
        assertTrue(keys.isEmpty(), "Expected keys list to be empty");
    }

    @Test
    void GivenNonEmptyHashMap_WhenGettingKeys_ThenReturnCorrectKeys() {
        hashMap.add("Key1", 1);
        hashMap.add("Key2", 2);
        SaxList<String> keys = hashMap.getKeys();
        assertTrue(keys.contains("Key1"), "Expected keys list to contain 'Key1'");
        assertTrue(keys.contains("Key2"), "Expected keys list to contain 'Key2'");
    }

    @Test
    void GivenHashMapWithCapacityLimit_WhenAddingElements_ThenCorrectlyExtendCapacity() {
        MyHashMap<String, Integer> limitedCapacityMap = new MyHashMap<>(1);
        limitedCapacityMap.add("Key1", 1);
        limitedCapacityMap.add("Key2", 2);
        assertEquals(2, limitedCapacityMap.size(), "Expected size to be 2 after capacity extension");
    }

    @Test
    void GivenHashMapWithExistingNode_WhenAddingNewNodeAtSameIndex_ThenLinkNodes() {
        MyHashMap<String, Integer> map = new MyHashMap<>(1);
        map.add("Key1", 1);
        map.add("Key2", 2);
        assertEquals(1, map.get("Key1"), "Expected value 1 for 'Key1'");
        assertEquals(2, map.get("Key2"), "Expected value 2 for 'Key2'");
    }

    @Test
    void GivenEmptyHashMap_WhenGettingIndexForKey_ThenReturnCorrectIndex() {
        int index = hashMap.getIndex("Key1");
        assertEquals("Key1".hashCode() & (32 - 1), index, "Expected correct index for 'Key1'");
    }

    @Test
    void GivenHashMapWithKey_WhenPuttingExistingKey_ThenUpdateValue() {
        hashMap.add("Key1", 1);
        hashMap.put("Key1", 2);
        assertEquals(2, hashMap.get("Key1"), "Expected updated value 2 for 'Key1'");
    }

    @Test
    void GivenHashMapWithCollision_WhenRemovingNodeWithLinkedNodes_ThenPreserveChain() {
        MyHashMap<String, Integer> map = new MyHashMap<>(1);
        map.add("Key1", 1);
        map.add("Key2", 2);
        map.remove("Key1");
        assertEquals(2, map.get("Key2"), "Expected value 2 for 'Key2' after 'Key1' removal");
    }

    @Test
    void GivenHashMapWithNode_WhenPuttingNewNode_ThenAddIfKeyNotExists() {
        hashMap.put("Key1", 1);
        assertEquals(1, hashMap.get("Key1"), "Expected new value 1 for 'Key1'");
        assertEquals(1, hashMap.size(), "Expected size 1 after put operation with new key");
    }

    @Test
    void GivenHashMapAtCapacity_WhenAddingElement_ThenTriggersSizeExtension() {
        MyHashMap<String, Integer> limitedCapacityMap = new MyHashMap<>(2);
        limitedCapacityMap.add("Key1", 1);
        limitedCapacityMap.add("Key2", 2);

        limitedCapacityMap.add("Key3", 3);

        assertEquals(3, limitedCapacityMap.size(), "Expected size to be 3 after adding elements beyond initial capacity");
        assertEquals(1, limitedCapacityMap.get("Key1"), "Expected value 1 for 'Key1' after resizing");
        assertEquals(2, limitedCapacityMap.get("Key2"), "Expected value 2 for 'Key2' after resizing");
        assertEquals(3, limitedCapacityMap.get("Key3"), "Expected value 3 for 'Key3' after resizing");
    }

    @Test
    void GivenHashMapWithCapacityLimit_WhenAddingElementsBeyondLimit_ThenCorrectlyExtendsAndPreservesElements() {
        MyHashMap<String, Integer> limitedCapacityMap = new MyHashMap<>(2);
        limitedCapacityMap.add("Key1", 1);
        limitedCapacityMap.add("Key2", 2);

        assertEquals(2, limitedCapacityMap.size(), "Expected initial size to match the capacity");
        assertEquals(1, limitedCapacityMap.get("Key1"), "Expected value 1 for 'Key1'");
        assertEquals(2, limitedCapacityMap.get("Key2"), "Expected value 2 for 'Key2'");

        limitedCapacityMap.add("Key3", 3);
        limitedCapacityMap.add("Key4", 4);

        assertEquals(4, limitedCapacityMap.size(), "Expected size to increase after adding more elements than initial capacity");
        assertEquals(1, limitedCapacityMap.get("Key1"), "Expected value 1 for 'Key1' after resizing");
        assertEquals(2, limitedCapacityMap.get("Key2"), "Expected value 2 for 'Key2' after resizing");
        assertEquals(3, limitedCapacityMap.get("Key3"), "Expected value 3 for 'Key3' after resizing");
        assertEquals(4, limitedCapacityMap.get("Key4"), "Expected value 4 for 'Key4' after resizing");
    }

    @Test
    void GivenHashMapAtInitialCapacity_WhenAddingElements_ThenInternalTableSizeIncreases() {
        int initialCapacity = 4;
        MyHashMap<String, Integer> map = new MyHashMap<>(initialCapacity);

        for (int i = 1; i <= initialCapacity; i++) {
            map.add("Key" + i, i);
        }
        assertEquals(initialCapacity, map.size(), "Expected map size to match initial capacity");

        map.add("Key" + (initialCapacity + 1), initialCapacity + 1);

        assertTrue(map.size() > initialCapacity, "Expected map size to exceed initial capacity after resizing");
        assertEquals(initialCapacity + 1, map.size(), "Expected size to reflect all elements after resizing");

        for (int i = 1; i <= initialCapacity + 1; i++) {
            assertEquals(i, map.get("Key" + i), "Expected correct value for 'Key" + i + "' after resizing");
        }
    }

    @Test
    void GivenHashMapWithCollision_WhenAddingNodesWithSameIndex_ThenCreateLinkedNodes() {
        MyHashMap<String, Integer> collisionMap = new MyHashMap<>(2);

        String key1 = "Aa";
        String key2 = "BB";
        collisionMap.add(key1, 1);
        collisionMap.add(key2, 2);

        SaxList<String> keys = collisionMap.getKeys();
        assertTrue(keys.contains(key1), "Expected keys to contain 'Aa'");
        assertTrue(keys.contains(key2), "Expected keys to contain 'BB'");
    }

}
