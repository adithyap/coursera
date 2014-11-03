import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by adithya on 20/10/14.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Deque<Item> deque;

    // construct an empty randomized queue
    public RandomizedQueue() {

        deque = new Deque<Item>();
    }

    // is the queue empty?
    public boolean isEmpty(){

        return deque.isEmpty();
    }

    // return the number of items on the queue
    public int size(){

        return deque.size();
    }

    // add the item
    public void enqueue(Item item){

        if(item == null)
            throw new NullPointerException("Null value attempted to enqueue into RandomizedQueue");

        deque.addLast(item);
    }

    // delete and return a random item
    public Item dequeue(){

        if(isEmpty())
            throw new NoSuchElementException("RandomizedQueue is empty, no element to dequeue");

        deque = GetRandomizedDeque(deque);

        return deque.removeFirst();
    }

    // return (but do not delete) a random item
    public Item sample(){

        if(isEmpty())
            throw new NoSuchElementException("RandomizedQueue is empty, no elements available to sample");

        deque = GetRandomizedDeque(deque);

        Item sample = deque.removeFirst();
        deque.addFirst(sample);
        return sample;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){

        Deque<Item> randomizedQueue = GetRandomizedDeque(deque);

        // Return iterator to the new Q
        return randomizedQueue.iterator();
    }

    private Deque<Item> GetRandomizedDeque(Deque<Item> deque){

        // Remove all elements from Q and insert into array
        int size = deque.size();
        Item[] items = (Item[]) new Object[size];
        int itemCount = 0;
        boolean[] added = new boolean[size];

        while (itemCount != size){

            // Remove and add it back
            Item i = deque.removeLast();
            deque.addFirst(i);

            items[itemCount++] = i;
        }

        // Re-init Q and randomly insert elements into Q
        Deque<Item> randomizedDeque = new Deque<Item>();

        while (randomizedDeque.size() != size){

            int randomIndex = StdRandom.uniform(0, size);

            if(added[randomIndex] == false){

                randomizedDeque.addFirst(items[randomIndex]);
                added[randomIndex] = true;
            }
        }

        return randomizedDeque;
    }

    // unit testing
//    public static void main(String[] args){
//
//    }
}
