package nl.saxion.cds.solution;


/**
 * Represents a node in a doubly linked list, which stores a value and references to the next and previous nodes.
 *
 * @param <V> the type of element stored in the node
 */
public class DLinkNode<V> {
    private V value;
    private DLinkNode<V> next, previous;

    /**
     * Constructs a node with references to the previous node, a value, and the next node.
     *
     * @param previous the previous node in the list
     * @param value the value to be stored in this node
     * @param next the next node in the list
     */
    DLinkNode(DLinkNode<V> previous, V value, DLinkNode<V> next) {
        this.value = value;
        this.next = next;
        this.previous = previous;
    }

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
