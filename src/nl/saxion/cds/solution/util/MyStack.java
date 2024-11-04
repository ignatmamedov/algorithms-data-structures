package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxStack;

/**
 * Custom stack implementation using an array list to store elements, supporting basic stack operations.
 *
 * @param <V> the type of elements stored in the stack
 */
public class MyStack<V> implements SaxStack<V> {
    private final MyArrayList<V> elements;

    /**
     * Initializes a new, empty stack.
     */
    public MyStack() {
        this.elements = new MyArrayList<>();
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
     * Add the value to the stack (on top).
     *
     * @param value the value to push
     */
    @Override
    public void push(V value) {
        this.elements.addLast(value);
    }

    /**
     * Remove the value from the stack (from top).
     *
     * @return the popped value
     */
    @Override
    public V pop() throws EmptyCollectionException {
        return this.elements.removeLast();
    }

    /**
     * Return the value on top of the stack, without removing it.
     *
     * @return the value or null if the stack is empty
     */
    @Override
    public V peek() throws EmptyCollectionException {
        if (this.elements.isEmpty()) {
            throw new EmptyCollectionException();
        }
        return this.elements.get(elements.size() - 1);
    }
}
