package nl.saxion.cds.solution;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxHeap;


public class MyHeap<V extends Comparable<V>> implements SaxHeap<V> {
    MyArrayList<V> elements;

    public MyHeap() {
        elements = new MyArrayList<>();
    }

    /**
     * Determines if the collection has no elements
     *
     * @return if the collection has no elements
     */
    @Override
    public boolean isEmpty() {

        return elements.isEmpty();
    }

    /**
     * Determines the number of elements in this collection
     *
     * @return size of this collection
     */
    @Override
    public int size() {
        return elements.size();
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
        return elements.graphViz(name);
    }

    /**
     * Add the value to list.
     *
     * @param value the value to push
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
     * Remove the value from the list.
     *
     * @return the popped value
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
     * Return the value of the list, without removing it.
     *
     * @return the value or null if the list is empty
     */
    @Override
    public V peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        return elements.get(0);
    }
}
