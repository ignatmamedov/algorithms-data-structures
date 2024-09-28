package nl.saxion.cds.collection;

public class ListNotSortedException extends RuntimeException {
    public ListNotSortedException() {
        super("The list is not sorted.");
    }
}
