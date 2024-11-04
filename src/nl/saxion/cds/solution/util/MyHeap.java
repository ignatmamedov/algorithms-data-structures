package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxHeap;

/**
 * Custom heap implementation that supports basic heap operations such as enqueue, dequeue, and peek.
 *
 * @param <V> the type of elements stored in the heap, which must be comparable
 */
public class MyHeap<V extends Comparable<V>> implements SaxHeap<V> {
    MyArrayList<V> elements;

    /**
     * Initializes a new heap.
     */
    public MyHeap() {
        elements = new MyArrayList<>();
    }

    /**
     * Determines if the collection has no elements.
     *
     * @return true if the collection has no elements, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Determines the number of elements in this collection.
     *
     * @return size of this collection
     */
    @Override
    public int size() {
        return elements.size();
    }

    /**
     * Creates a String representation of the data in GraphViz format.
     * Useful for visualizing the heap structure.
     *
     * @param name name of the produced graph
     * @return a GraphViz string representation of this collection
     */
    @Override
    public String graphViz(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph ").append(name).append(" {\n");
        sb.append("    node [shape=circle];\n");

        for (int i = 0; i < elements.size(); i++) {
            sb.append("    \"").append(elements.get(i)).append("\";\n");
        }

        for (int i = 0; i < elements.size(); i++) {
            int leftChildIndex = 2 * i + 1;
            int rightChildIndex = 2 * i + 2;

            if (leftChildIndex < elements.size()) {
                sb.append("    \"").append(elements.get(i)).append("\" -> \"")
                        .append(elements.get(leftChildIndex)).append("\";\n");
            }
            if (rightChildIndex < elements.size()) {
                sb.append("    \"").append(elements.get(i)).append("\" -> \"")
                        .append(elements.get(rightChildIndex)).append("\";\n");
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Adds a value to the heap, maintaining the heap property.
     *
     * @param value the value to be added to the heap
     */
    @Override
    public void enqueue(V value) {
        int indexCurrent = elements.size();
        elements.addLast(value);

        while (indexCurrent != 0) {
            int indexParent = (indexCurrent - 1) / 2;
            V parent = elements.get(indexParent);
            if (value.compareTo(parent) >= 0) {
                break;
            }
            elements.set(indexCurrent, parent);
            indexCurrent = indexParent;
        }

        elements.set(indexCurrent, value);
    }

    /**
     * Removes and returns the root of the heap.
     *
     * @return the root value of the heap
     * @throws EmptyCollectionException if the heap is empty
     */
    @Override
    public V dequeue() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        V root = elements.get(0);
        V last = elements.removeLast();

        if (!isEmpty()) {
            elements.set(0, last);
            int indexCurrent = 0;
            int indexLeft = 1;

            while (indexLeft < elements.size()) {
                int indexRight = indexLeft + 1;
                int indexMin = indexLeft;

                if (indexRight < elements.size() && elements.get(indexRight).compareTo(elements.get(indexLeft)) < 0) {
                    indexMin = indexRight;
                }

                if (elements.get(indexCurrent).compareTo(elements.get(indexMin)) <= 0) {
                    break;
                }

                elements.swap(indexCurrent, indexMin);
                indexCurrent = indexMin;

                indexLeft = 2 * indexCurrent + 1;
            }
        }
        return root;
    }

    /**
     * Returns the root value of the heap without removing it.
     *
     * @return the root value of the heap
     * @throws EmptyCollectionException if the heap is empty
     */
    @Override
    public V peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        return elements.get(0);
    }
}
