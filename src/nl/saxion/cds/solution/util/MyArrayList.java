package nl.saxion.cds.solution.util;

import nl.saxion.cds.collection.*;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Custom implementation of an ArrayList, providing functionality for dynamic resizing, search, and sorting.
 *
 * @param <V> the type of elements stored in the list
 */
public class MyArrayList<V> implements SaxList<V>, SaxSearchable<V>, SaxSortable<V> {
    // Minimal size of the internal array
    private static final int MINIMUM_SIZE = 32;
    // Extending means doubling in size, until the size is bigger than this maximum extension size
    private static final int MAXIMUM_EXTENSION = 256;

    // Java prohibits creating an array with a generic type, so we use Object
    private Object[] elements;
    // Number of elements in use
    private int size;

    /**
     * Initializes a new ArrayList with the minimum capacity.
     */
    public MyArrayList() {
        this(MINIMUM_SIZE);
    }

    /**
     * Initializes a new ArrayList with the specified capacity.
     *
     * @param capacity initial capacity of the array list
     */
    public MyArrayList(int capacity) {
        this.size = 0;
        elements = new Object[capacity];
    }

    /**
     * Checks if the specified value is in the list.
     *
     * @param value the value to search for
     * @return true if the value is in the list, false otherwise
     */
    @Override
    // Do no type checking; a Java hack, because we store objects of a generic type V in an Object array
    @SuppressWarnings("unchecked")
    public boolean contains(V value) {
        for (int i = 0; i < size; ++i) {
            V element = (V) elements[i];
            if (element.equals(value)) return true;
        }
        return false;
    }

    /**
     * Retrieves the element at the specified index.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    @SuppressWarnings("unchecked")
    public V get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        return (V) elements[index];
    }

    /**
     * Adds a value to the end of the list.
     *
     * @param value the value to add
     */
    @Override
    public void addLast(V value) {
        checkAndExtendSize(size);  // Ignore invalid index => IndexOutOfBoundsException
        elements[size - 1] = value;
    }

    /**
     * Adds a value to the beginning of the list.
     *
     * @param value the value to add
     */
    @Override
    public void addFirst(V value) {
        checkAndExtendSize(0); // Ignore invalid index => IndexOutOfBoundsException
        elements[0] = value;
    }

    /**
     * Inserts a value at the specified index.
     *
     * @param index the index at which to insert the value
     * @param value the value to add
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void addAt(int index, V value) throws IndexOutOfBoundsException {
        checkAndExtendSize(index); // Ignore invalid index => IndexOutOfBoundsException
        elements[index] = value;
    }

    /**
     * Sets the element at the specified index.
     *
     * @param index the index at which to set the value
     * @param value the value to set
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public void set(int index, V value) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Integer.toString(index));
        elements[index] = value;
    }

    /**
     * Removes and returns the last element in the list.
     *
     * @return the removed element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    // Do no type checking; a Java hack, because we store objects of a generic type V in an Object array
    @SuppressWarnings("unchecked")
    public V removeLast() throws EmptyCollectionException {
        if (isEmpty()) throw new EmptyCollectionException();
        V value = (V) elements[--size];
        elements[size] = null; // this element no longer contains valid info
        return value;
    }

    /**
     * Removes and returns the first element in the list.
     *
     * @return the removed element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    // Do no type checking; a Java hack, because we store objects of a generic type V in an Object array
    @SuppressWarnings("unchecked")
    public V removeFirst() throws EmptyCollectionException {
        if (isEmpty()) throw new EmptyCollectionException();
        V value = (V) elements[0];
        removeAt(0);
        return value;
    }

    /**
     * Removes and returns the element at the specified index.
     *
     * @param index the index of the element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    // Do no type checking; a Java hack, because we store objects of a generic type V in an Object array
    @SuppressWarnings("unchecked")
    public V removeAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException(Integer.toString(index));
        V value = (V) elements[index];
        if (index < --size) {
            // shift all element one to the left (removing the element to delete)
            System.arraycopy(elements, index + 1, elements, index, size - index);
        }
        elements[size] = null; // this element no longer contains valid info
        return value;
    }

    /**
     * Removes the specified value from the list.
     *
     * @param value the value to remove
     * @throws ValueNotFoundException if the value is not found in the list
     */
    @Override
    public void remove(V value) throws ValueNotFoundException {
        int index = 0;
        for (var anElement : this) {
            if (anElement == null) {
                if (value == null) break; // found null value
            } else {
                if (anElement.equals(value)) break; // found value
            }
            ++index;
        }
        if (index == size)
            throw new ValueNotFoundException(value == null ? "null" : value.toString());
        removeAt(index);
    }

    /**
     * Determines if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
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
     * Creates a GraphViz representation of the list.
     *
     * @param name the name of the produced graph
     * @return a GraphViz string representation of the list
     */
    @Override
    // Do no type checking; a Java hack, because we store objects of a generic type V in an Object array
    @SuppressWarnings("unchecked")
    public String graphViz(String name) {
        var builder = new StringBuilder();
        builder.append("digraph ");
        builder.append(name);
        builder.append(" {\n");
        for (int i = 0; i < size - 1; ++i) {
            V from = (V) elements[i];
            V to = (V) elements[i + 1];
            builder.append(String.format("\"%s\" -> \"%s\"\n", (from == null ? "NULL_" + i : from.toString()), (to == null ? "NULL_" + (i + 1) : to.toString())));
        }
        builder.append("}");
        return builder.toString();
    }

    /**
     * Check if the array of elements can hold another element and if not extend the array.
     * Make room on position index and adjust size.
     *
     * @param index position where to make room for a new element, valid 0.. size (size == add at end)
     * @throws IndexOutOfBoundsException index < 0 or > size
     */
    private void checkAndExtendSize(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException(Integer.toString(index));
        if (elements.length < size + 1) {
            // extend array by doubling in size if size is smaller ten maximum extension
            int capacity = elements.length < MAXIMUM_EXTENSION ? elements.length * 2 : elements.length + MAXIMUM_EXTENSION;
            var newElements = new Object[capacity];
            // copy existing elements
            System.arraycopy(elements, 0, newElements, 0, size);
            elements = newElements;
        }
        if (index < size) {
            // Make room for the new element
            System.arraycopy(elements, index, elements, index + 1, size - index);
        }
        ++size;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; i < size; ++i) {
            builder.append(' ');
            builder.append(elements[i]);
        }
        builder.append(" ]");
        return builder.toString();
    }

    /**
     * Checks if the list is sorted based on the specified comparator.
     *
     * @param comparator comparator to determine the sorting order
     * @return true if the list is sorted, false otherwise
     */
    @Override
    public boolean isSorted(Comparator<V> comparator) {
        // TODO: implement isSorted()
        for (int index =0; index < this.size() - 1;  index++){
            V current = this.get(index);
            V next = this.get(index + 1);

            if (current == null && next != null) {
                return false;
            }

            if (next != null){
                if (comparator.compare(current, next) > 0){
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * Do a selection sort (in place) on the elements in ascending order.
     */
    @Override
    public void simpleSort(Comparator<V> comparator) {
        for (int index = 0; index < size; ++index) {
            // search for smallest element in the sequence between smallest and seqLength
            int smallest = index;
            for (int index2 = index; index2 < size; ++index2) {
                if (comparator.compare(get(smallest), get(index2)) > 0) {
                    smallest = index2;
                }
            }
            // swap smallest element with element at smallest
            swap(index, smallest);
        }
    }

    /**
     * Swap the elements on the given position.
     *
     * @param index1 first element index
     * @param index2 second element index
     */
    protected void swap(int index1, int index2) {
        assert index1 >= 0 && index1 < size;
        assert index2 >= 0 && index2 < size;

        if (index1 != index2) {
            var temp = elements[index1];
            elements[index1] = elements[index2];
            elements[index2] = temp;
        }
    }

    /**
     * Recursively do a quick sort (in place) on the elements in ascending order.
     */
    @Override
    public void quickSort(Comparator<V> comparator) {
        quickSort(comparator, 0, size - 1);
    }

    /**
     * Recursively do a quick sort (in place) on the elements from begin until (including) end in ascending order.
     *
     * @param comparator method to compare two V objects
     * @param begin      start of range
     * @param end        end of range
     */
    private void quickSort(Comparator<V> comparator, int begin, int end) {
        if (end - begin >= 1) {
            int pivot = splitInPlace(comparator, begin, end);
            quickSort(comparator, begin, pivot - 1);
            quickSort(comparator, pivot + 1, end);
        }
    }

    /**
     * Split the range of elements into 2 parts; on the left the elements which are smaller
     * than the pivot, on the right te elements which are bigger then the pivot.
     * Start with the first element is the pivot value and return the index of the pivot afterward.
     *
     * @param comparator method to compare two objects
     * @param begin      left index
     * @param end        right index
     * @return the current index of the pivot
     */
    private int splitInPlace(Comparator<V> comparator, int begin, int end) {
        V pivot = get(begin); // first element (at begin) as pivot

        int left = begin;
        int right = end;
        int index = right;

        // TODO: complete splitInPlace()
        while (left <= right) {
            if (left <= end && comparator.compare(this.get(left), pivot) <= 0) {
                left++;
            }
            if (right >= begin && comparator.compare(this.get(right), pivot) > 0) {
                right--;
            }
            if (left < right) {
                this.swap(left, right);
            }
        }
        this.swap(begin, right);

        return right;
    }

    /**
     * Searches for the specified element using linear search.
     *
     * @param element the element to search for
     * @return the index of the element if found, or NOT_FOUND if not found
     */
    @Override
    public int linearSearch(Object element) {
        for (int i = 0; i < size; ++i) {
            if (elements[i].equals(element)) {
                return i;
            }
        }
        return SaxSearchable.NOT_FOUND;
    }

    /**
     * Performs a binary search for the specified element using the given comparator.
     *
     * @param element     element to search for
     * @param comparator comparator to determine the order of elements
     * @throws ListNotSortedException if list is not sorted
     * @return the index of the element if found, otherwise - 1
     */
    @Override
    public int binarySearch(Comparator<V> comparator, V element) {
        // TODO: implement binarySearch()
        if (!this.isSorted(comparator)){
            throw new ListNotSortedException();
        }

        int left = 0;
        int right = this.size();
        int middle = (left + right) / 2;
        while (left < right){
            if (comparator.compare(this.get(middle), element) == 0){
                return middle;
            }
            if (comparator.compare(this.get(middle), element) < 0){
                left = middle + 1;
            }
            else {
                right = middle;
            }
            middle = (left + right) / 2;
        }
        return SaxSearchable.NOT_FOUND;
    }

    @Override
    public Iterator<V> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            // Do no type checking; a Java hack, because we store objects of a generic type V in an Object array
            public V next() {
                return (V) elements[currentIndex++];
            }

            // Remove is not necessary; just for demonstration purposes / as a challenge
            @Override
            public void remove() {
                MyArrayList.this.removeAt(currentIndex - 1);
            }
        };
    }
}
