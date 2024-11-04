package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.collection.SaxList;
import nl.saxion.cds.collection.ValueNotFoundException;

import java.util.Iterator;

/**
 * A doubly linked list implementation of the SaxList interface.
 *
 * @param <V> the type of elements stored in this list
 */
public class DLinkedList<V> implements SaxList<V> {

    private DLinkNode<V> head = null, tail = null;
    private int size;

    /**
     * Checks if the list contains a specific value.
     *
     * @param value the value to be checked
     * @return {@code true} if the value is found, {@code false} otherwise
     */
    @Override
    public boolean contains(V value) {
        DLinkNode<V> current = head;
        for (int i = 0; i < size(); ++i) {
            if (current.getValue().equals(value)){
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Returns the value at a specific index.
     *
     * @param index the index of the value to be retrieved
     * @return the value at the given index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public V get(int index) throws IndexOutOfBoundsException {
        DLinkNode<V> current = head;
        V value;

        if (index >=0 && index < this.size()) {
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            value = current.getValue();
        }
        else {
            throw new IndexOutOfBoundsException();
        }
        return value;
    }

    /**
     * Adds a value to the end of the list.
     *
     * @param value the value to be added
     */
    @Override
    public void addLast(V value) {
        DLinkNode<V> newNode = new DLinkNode<>(value);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
        }
        tail = newNode;
        size++;
    }

    /**
     * Adds a value to the beginning of the list.
     *
     * @param value the value to be added
     */
    @Override
    public void addFirst(V value) {
        DLinkNode<V> newNode = new DLinkNode<>(value);
        if (!isEmpty()) {
            newNode.setNext(head);
            head.setPrev(newNode);
        }
        head = newNode;
        if (tail == null) {
            tail = head;
        }
        size++;

    }

    /**
     * Adds a value at a specific index in the list.
     *
     * @param index the index at which the value should be inserted
     * @param value the value to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void addAt(int index, V value) throws IndexOutOfBoundsException {
        if (index < 0 || index > this.size()){
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            addFirst(value);
        } else if (index == size) {
            addLast(value);
        } else {
            DLinkNode<V> previous = head;
            for (int i = 1; i < index; i++) {
                previous = previous.getNext();
            }

            DLinkNode<V> current = new DLinkNode<>(value);
            DLinkNode<V> nextNode = previous.getNext();
            current.setNext(nextNode);
            nextNode.setPrev(current);
            previous.setNext(current);
            current.setPrev(previous);
            size++;
        }

    }

    /**
     * Sets a value at a specific index in the list.
     *
     * @param index the index to set the value at
     * @param value the value to be set
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void set(int index, V value) throws IndexOutOfBoundsException {
        DLinkNode<V> current = head;
        if (index >=0 && index < this.size()) {
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            current.setValue(value);
        }
        else {
            throw new IndexOutOfBoundsException();
        }

    }

    /**
     * Removes the last element from the list.
     *
     * @return the value of the removed element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public V removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        if (size == 1) {
            return removeFirst();
        }
        V value = tail.getValue();
        DLinkNode<V> prev = tail.getPrev();
        prev.setNext(null);
        this.tail = prev;
        size--;
        return value;
    }

    /**
     * Removes the first element from the list.
     *
     * @return the value of the removed element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public V removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException();
        }
        V value = head.getValue();
        this.head = head.getNext();
        if (head == null) {
            this.tail = null;
        } else {
            this.head.setPrev(null);
        }
        size--;
        return value;
    }

    /**
     * Removes an element at a specific index from the list.
     *
     * @param index the index of the element to be removed
     * @return the value of the removed element
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public V removeAt(int index) throws IndexOutOfBoundsException {
        DLinkNode<V> current = head;
        if (index >=0 && index < this.size()) {
            if (index == 0) {
                return removeFirst();
            }
            if (index == this.size() - 1){
                return removeLast();
            }
            for (int i = 1; i <= index; i++) {
                current = current.getNext();
            }

            DLinkNode<V> previous = current.getPrev();
            DLinkNode<V> next = current.getNext();
            previous.setNext(next);
            next.setPrev(previous);
            size--;
            return current.getValue();

        }
        else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Removes a specific value from the list.
     *
     * @param value the value to be removed
     * @throws ValueNotFoundException if the value is not found in the list
     */
    @Override
    public void remove(V value) throws ValueNotFoundException {
        boolean exist = false;
        DLinkNode<V> current = head;
        for (int i = 0; i < size(); ++i) {
            if (current.getValue().equals(value)){
                exist = true;
                if (i == 0) {
                    removeFirst();
                    break;
                }
                else if (i == size - 1) {
                    removeLast();
                    break;
                }
                else {
                    DLinkNode<V> previous = current.getPrev();
                    DLinkNode<V> next = current.getNext();
                    previous.setNext(next);
                    next.setPrev(previous);
                    size--;
                    break;
                }
            }
            current = current.getNext();
        }
        if (!exist){
            throw new ValueNotFoundException(value.toString());
        }
    }

    /**
     * Returns an iterator over the elements in the list.
     *
     * @return an iterator over the elements in this list
     */
    @Override
    public Iterator<V> iterator() {
        return new DLinkIterator<>(this.head);
    }

    /**
     * Checks if the list is empty.
     *
     * @return {@code true} if the list is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the number of elements in the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Generates a GraphViz representation of the linked list with a specified name.
     *
     * @param name the name to be used in the GraphViz graph
     * @return a string containing the GraphViz representation of the list
     */
    @Override
    public String graphViz(String name) {
        var builder = new StringBuilder();
        builder.append("digraph ");
        builder.append(name);
        builder.append(" {\n");
        DLinkNode<V> current = head;
        for (int i = 0; i < size - 1; ++i) {
            V from = current.getValue();
            current = current.getNext();
            V to = current.getValue();
            builder.append(String.format("\"%s\" -> \"%s\"\n", (from == null ? "NULL_" + i : from.toString()), (to == null ? "NULL_" + (i + 1) : to.toString())));
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * Represents a node in a doubly linked list, which stores a value and references to the next and previous nodes.
     *
     * @param <V> the type of element stored in the node
     */
    private class DLinkNode<V> {
        private V value;
        private DLinkNode<V> next, previous;

        /**
         * Constructs a node with the specified value and no references to other nodes.
         *
         * @param value the value to be stored in this node
         */
        public DLinkNode(V value) {
            this.value = value;
        }

        /**
         * Returns the next node in the list.
         *
         * @return the next node
         */
        public DLinkNode<V> getNext() {
            return next;
        }

        /**
         * Sets the reference to the next node in the list.
         *
         * @param next the node to be set as the next node
         */
        public void setNext(DLinkNode<V> next) {
            this.next = next;
        }

        /**
         * Returns the previous node in the list.
         *
         * @return the previous node
         */
        public DLinkNode<V> getPrev() {
            return previous;
        }

        /**
         * Sets the reference to the previous node in the list.
         *
         * @param previous the node to be set as the previous node
         */
        public void setPrev(DLinkNode<V> previous) {
            this.previous = previous;
        }

        /**
         * Returns the value stored in this node.
         *
         * @return the value stored in the node
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets the value to be stored in this node.
         *
         * @param value the value to be set
         */
        public void setValue(V value) {
            this.value = value;
        }
    }

    /**
     * An iterator for traversing a doubly linked list.
     *
     * @param <T> the type of elements returned by this iterator
     */
    private class DLinkIterator<T> implements Iterator<T> {
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
}
