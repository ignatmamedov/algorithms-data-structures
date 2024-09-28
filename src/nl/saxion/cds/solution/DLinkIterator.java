package nl.saxion.cds.solution;

import java.util.Iterator;


/**
 * An iterator for traversing a doubly linked list.
 *
 * @param <T> the type of elements returned by this iterator
 */
public class DLinkIterator<T> implements Iterator<T> {
    private DLinkNode<T> current;

    /**
     * Constructs an iterator starting at the given head node.
     *
     * @param head the first node of the list to iterate over
     */
    DLinkIterator(DLinkNode<T> head) {
        current = head;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return current != null;
    }


    /**
     * Returns the next element in the iteration and advances the iterator.
     *
     * @return the next element in the iteration
     */
    @Override
    public T next() {
        T result = current.getValue();
        current = current.getNext();
        return result;
    }
}