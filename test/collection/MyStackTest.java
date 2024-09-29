package collection;

import nl.saxion.cds.collection.EmptyCollectionException;
import nl.saxion.cds.solution.MyStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MyStackTest {

    private MyStack<String> stack;

    @BeforeEach
    void setUp() {
        stack = new MyStack<>();
    }

    @Test
    void givenEmptyStack_whenIsEmptyCalled_thenReturnsTrue() {
        assertTrue(stack.isEmpty(), "Expected stack to be empty");
    }

    @Test
    void givenStackWithOneElement_whenIsEmptyCalled_thenReturnsFalse() {
        stack.push("element1");
        assertFalse(stack.isEmpty(), "Expected stack to not be empty after pushing an element");
    }

    @Test
    void givenEmptyStack_whenSizeCalled_thenReturnsZero() {
        assertEquals(0, stack.size(), "Expected size of empty stack to be zero");
    }

    @Test
    void givenStackWithTwoElements_whenSizeCalled_thenReturnsTwo() {
        stack.push("element1");
        stack.push("element2");
        assertEquals(2, stack.size(), "Expected size of stack with two elements to be two");
    }

    @Test
    void givenEmptyStack_whenPopCalled_thenThrowsEmptyCollectionException() {
        assertThrows(EmptyCollectionException.class, stack::pop, "Expected pop to throw EmptyCollectionException when stack is empty");
    }

    @Test
    void givenStackWithOneElement_whenPopCalled_thenReturnsElementAndStackIsEmpty() throws EmptyCollectionException {
        stack.push("element1");
        String popped = stack.pop();
        assertEquals("element1", popped, "Expected pop to return the pushed element");
        assertTrue(stack.isEmpty(), "Expected stack to be empty after popping the only element");
    }

    @Test
    void givenEmptyStack_whenPeekCalled_thenThrowsEmptyCollectionException() {
        assertThrows(EmptyCollectionException.class, stack::peek, "Expected peek to throw EmptyCollectionException when stack is empty");
    }

    @Test
    void givenStackWithOneElement_whenPeekCalled_thenReturnsElementWithoutRemovingIt() throws EmptyCollectionException {
        stack.push("element1");
        String topElement = stack.peek();
        assertEquals("element1", topElement, "Expected peek to return the pushed element");
        assertFalse(stack.isEmpty(), "Expected stack to not be empty after peeking");
    }

    @Test
    void givenStackWithMultipleElements_whenPeekCalled_thenReturnsTopElementWithoutRemovingIt() throws EmptyCollectionException {
        stack.push("element1");
        stack.push("element2");
        stack.push("element3");
        String topElement = stack.peek();
        assertEquals("element3", topElement, "Expected peek to return the top element 'element3'");
        assertEquals(3, stack.size(), "Expected stack size to remain unchanged after peeking");
    }

    @Test
    void givenStackWithMultipleElements_whenPopCalledRepeatedly_thenReturnsElementsInLIFOOrder() throws EmptyCollectionException {
        stack.push("element1");
        stack.push("element2");
        stack.push("element3");

        assertEquals("element3", stack.pop(), "Expected pop to return 'element3' first");
        assertEquals("element2", stack.pop(), "Expected pop to return 'element2' second");
        assertEquals("element1", stack.pop(), "Expected pop to return 'element1' third");
        assertTrue(stack.isEmpty(), "Expected stack to be empty after popping all elements");
    }

    @Test
    void givenStack_whenGraphVizCalledWithValidName_thenReturnsGraphVizString() {
        stack.push("element1");
        stack.push("element2");
        String graph = stack.graphViz("TestGraph");
        assertTrue(graph.contains("TestGraph"), "Expected graphViz output to include the graph name 'TestGraph'");
    }
}
