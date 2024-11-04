package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.*;

/**
 * Custom hash map implementation that stores key-value pairs and handles collisions using chaining.
 *
 * @param <K> the type of keys in the hash map
 * @param <V> the type of values in the hash map
 */
public class MyHashMap<K, V> implements SaxHashMap<K, V> {
    private int size;
    private Node<K,V>[] table;
    private static final int MINIMUM_SIZE = 32;

    private final MyArrayList<K> keys;

    /**
     * Initializes a new hash map with a default capacity.
     */
    public MyHashMap() {
        this(MINIMUM_SIZE);
    }

    /**
     * Initializes a new hash map with a given capacity.
     *
     * @param capacity the initial capacity of the hash map
     */
    public MyHashMap(int capacity) {
        this.size = 0;
        table = (Node<K, V>[]) new Node[capacity];
        keys = new MyArrayList<> ();
    }

    /**
     * Computes the index in the hash table for the given key based on its hash code.
     *
     * @param key the key whose index is to be calculated
     * @return the index corresponding to the key
     */
    public int getIndex(K key){
        return (table.length - 1) & key.hashCode();
    }

    /**
     * Determines if the collection has no elements
     *
     * @return if the collection has no elements
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Determines the number of elements in this collection
     *
     * @return size of this collection
     */
    @Override
    public int size() {
        return this.size;
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
        builder.append("digraph ");
        builder.append(name);
        builder.append(" {\n");

        for (int i = 0; i < table.length; i++) {
            Node<K, V> node = table[i];
            if (node != null) {
                builder.append(i).append(": ");

                while (node != null) {
                    String currentNode = String.format(
                            "\"%s=%s\"", node.getKey().toString(), node.getValue().toString()
                    );
                    builder.append(currentNode);

                    if (node.getNext() != null) {
                        builder.append(" -> ");
                    }
                    node = node.getNext();
                }
                builder.append(";\n");
            }
        }

        builder.append("}");
        return builder.toString();
    }

    /**
     * Check if the key is part of this map.
     * Uses K.equals() to check for equality.
     *
     * @param key key to search for
     * @return if the key is in this collection
     */
    @Override
    public boolean contains(K key) {
        return getNode(key) != null;
    }

    /**
     * Get a value which is mapped to the key.
     *
     * @param key key which is mapped to value to be found
     * @return the value mapped to the key or null if the key is not found
     */
    @Override
    public V get(K key) {
        Node<K, V> node = getNode(key);
        if (node != null){
            return node.getValue();
        }
        return null;
    }

    /**
     * Retrieves the node associated with a specified key.
     *
     * @param key the key whose associated node is to be returned
     * @return the node containing the specified key, or null if the key is not found
     */
    public Node<K, V> getNode(K key) {
        int index = getIndex(key);
        Node<K, V> node = table[index];
        while (node != null){
            if(node.getKey().equals(key)){
                return node;
            }
            node = node.getNext();
        }
        return null;
    }

    /**
     * Add the value which will be mapped to the key.
     * A duplicate key will throw a DuplicateKeyException.
     *
     * @param key   key which is mapped to value
     * @param value the value to add
     * @throws DuplicateKeyException if the key is already part of the collection
     */
    @Override
    public void add(K key, V value) throws DuplicateKeyException {
        if (this.contains(key)){
            throw new DuplicateKeyException(key.toString());
        }
        keys.addLast(key);
        Node<K, V> current = new Node<>(key, value);
        checkAndExtendSize();
        int index = getIndex(key);
        if (table[index] == null){
            table[index] = current;
        } else {
            Node<K, V> previous = table[index];
            while (previous.getNext() != null){
                previous = previous.getNext();
            }
            previous.setNext(current);
        }
    }

    /**
     * Associates the specified value with the specified key. If the key exists, the value is updated.
     *
     * @param key   the key with which the specified value is to be associated
     * @param value the value to be associated with the key
     */
    public void put(K key, V value) {
        if (this.contains(key)) {
            Node<K, V> node = getNode(key);
            if (node != null) {
                node.setValue(value);
            }
        } else {
            add(key, value);
        }
    }

    /**
     * Remove the value which is mapped with the key from the collection
     *
     * @param key key which is mapped to value
     * @return the value which is removed from the collection
     * @throws KeyNotFoundException if the key is not part oif the collection
     */
    @Override
    public V remove(K key) throws KeyNotFoundException {
        if (!this.contains(key)){
            throw new KeyNotFoundException(key.toString());
        }
        int index = getIndex(key);
        Node<K, V> node = table[index];
        Node<K, V> previous = null;

        while (node != null) {
            if (node.getKey().equals(key)) {
                keys.remove(key);
                size--;
                if (previous == null) {
                    table[index] = node.getNext();
                } else {
                    previous.setNext(node.getNext());
                }
                return node.getValue();
            }
            previous = node;
            node = node.getNext();
        }
        return null;
    }

    /**
     * Retrieves a collection of all keys present in the map.
     *
     * @return a list containing all keys in this map
     */
    @Override
    public SaxList<K> getKeys() {
        return keys;
    }

    /**
     * Check if the array of elements can hold another element and if not extend the array.
     * Make room on position index and adjust size.
     *
     * @throws IndexOutOfBoundsException index < 0 or > size
     */
    private void checkAndExtendSize() {
        if (size >= table.length) {
            int newCapacity = table.length * 2;
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];

            for (Node<K, V> node : table) {
                while (node != null) {
                    Node<K, V> nextNode = node.getNext();
                    int newIndex = (newCapacity - 1) & node.getKey().hashCode();
                    node.setNext(newTable[newIndex]);
                    newTable[newIndex] = node;
                    node = nextNode;
                }
            }
            table = newTable;
        }
        size++;
    }

    /**
     * Represents a node in the hash map that stores a key-value pair and a reference to the next node.
     *
     * @param <K> type of keys
     * @param <V> type of values
     */
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K,V> next;

        /**
         * Gets the next node in the linked list.
         *
         * @return the next node or null if there is no next node
         */
        private Node<K,V> getNext() {
            return next;
        }

        /**
         * Sets the next node in the linked list.
         *
         * @param next the next node to be set
         */
        private void setNext(Node<K,V> next) {
            this.next = next;
        }

        /**
         * Gets the key stored in this node.
         *
         * @return the key of this node
         */
        public final K getKey(){
            return key;
        }

        /**
         * Gets the value stored in this node.
         *
         * @return the value of this node
         */
        public final V getValue(){
            return value;
        }

        /**
         * Initializes a new node with the given key and value.
         *
         * @param key   the key of the node
         * @param value the value of the node
         */
        public Node(K key, V value){
            this.key = key;
            this.value = value;
        }

        /**
         * Sets the value for this node.
         *
         * @param value the new value to set
         */
        public void setValue(V value){
            this.value = value;
        }

    }
}
