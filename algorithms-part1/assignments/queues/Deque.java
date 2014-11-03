import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by adithya on 20/10/14.
 */
public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;

    private int size;

    private class Node{
        Item item;
        Node previous;
        Node next;
    }

    // construct an empty deque
    public Deque() {

        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {

        return size == 0;
    }

    // return the number of items on the deque
    public int size() {

        return size;
    }

    // insert the item at the front
    public void addFirst(Item item) {

        if(item == null)
            throw new NullPointerException("Null value attempted to addFirst into Deque");

        Node oldFirst = first;

        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.previous = null;

        size++;

        if(last == null)
            last = first;

        if(first.next != null) {
            // Set previous of next node
            first.next.previous = first;
        }

        // De allocate
        oldFirst = null;
    }

    // insert the item at the end
    public void addLast(Item item) {

        if(item == null)
            throw new NullPointerException("Null value attempted to addLast into Deque");

        Node oldLast = last;

        last = new Node();
        last.item = item;
        last.previous = oldLast;
        last.next = null;

        size++;

        if(first == null)
            first = last;

        if(last.previous != null) {
            // Set next of previous node
            last.previous.next = last;
        }

        // De allocate
        oldLast = null;
    }

    // delete and return the item at the front
    public Item removeFirst() {

        if(isEmpty())
            throw new NoSuchElementException("Deque is empty, no element to removeFirst");

        Node oldFirst = first;
        Item item = oldFirst.item;

        if(size == 1) {
            last = null;
            first = null;
        }
        else {
            first = first.next;
            first.previous = null;
        }

        size--;

        // De allocate
        oldFirst = null;

        return item;
    }

    // delete and return the item at the end
    public Item removeLast() {

        if(isEmpty())
            throw new NoSuchElementException("Deque is empty, no element to removeLast");

        Node oldLast = last;
        Item item = oldLast.item;

        if(size == 1) {
            last = null;
            first = null;
        }
        else {
            last = last.previous;
            last.next = null;
        }

        size--;

        // De allocate
        oldLast = null;

        return item;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {

        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>{

        Node current = first;

        @Override
        public boolean hasNext() {

            if(current != null)
                return true;

            return false;
        }

        @Override
        public Item next() {

            if(current == null)
                throw new NoSuchElementException("No element left in Deque to return");

            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {

            throw new UnsupportedOperationException("Remove element is null is not supported in Deque");

        }
    }

    // unit testing
//    public static void main(String[] args) {
//
//    }
}