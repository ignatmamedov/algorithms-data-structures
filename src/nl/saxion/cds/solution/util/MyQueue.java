package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxQueue;

/**
 * Custom queue implementation using a doubly linked list to store elements, supporting basic queue operations.
 *
 * @param <V> the type of elements stored in the queue
 */
public class MyQueue<V> implements SaxQueue<V> {
    private final DLinkedList<V> elements;

    /**
     * Initializes a new, empty queue.
     */
    public MyQueue(){
        this.elements = new DLinkedList<>();
    }

    /**
     * Determines if the collection has no elements
     *
     * @return if the collection has no elements
     */
    @Override
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    /**
     * Determines the number of elements in this collection
     *
     * @return size of this collection
     */
    @Override
    public int size() {
        return this.elements.size();
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
        return this.elements.graphViz(name);
    }

    /**
     * Add the value to list.
     *
     * @param value the value to push
     */
    @Override
    public void enqueue(V value) {
        this.elements.addLast(value);
    }

    /**
     * Remove the value from the list.
     *
     * @return the popped value
     */
    @Override
    public V dequeue() throws EmptyCollectionException {
        if (this.elements.isEmpty()){
            throw new EmptyCollectionException();
        }
        return this.elements.removeFirst();
    }

    /**
     * Return the value of the list, without removing it.
     *
     * @return the value or null if the list is empty
     */
    @Override
    public V peek() throws EmptyCollectionException {
        if (this.elements.isEmpty()){
            throw new EmptyCollectionException();
        }
        return this.elements.get(0);
    }
}
