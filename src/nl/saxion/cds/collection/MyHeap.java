package nl.saxion.cds.collection;

import nl.saxion.cds.solution.MyArrayList;

public class MyHeap<V extends Comparable<V>> implements SaxHeap<V> {
    MyArrayList<V> elements;
    /**
     * Determines if the collection has no elements
     *
     * @return if the collection has no elements
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * Determines the number of elements in this collection
     *
     * @return size of this collection
     */
    @Override
    public int size() {
        return 0;
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
        return null;
    }

    /**
     * Add the value to list.
     *
     * @param value the value to push
     */
    @Override
    public void enqueue(V value) {

    }

    /**
     * Remove the value from the list.
     *
     * @return the popped value
     */
    @Override
    public V dequeue() throws EmptyCollectionException {
        return null;
    }

    /**
     * Return the value of the list, without removing it.
     *
     * @return the value or null if the list is empty
     */
    @Override
    public V peek() throws EmptyCollectionException {
        return null;
    }
}
