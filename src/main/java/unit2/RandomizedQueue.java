package unit2;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] items = (Item[]) new Object[1];

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        assertNotNull(item);

        if (size == items.length) {
            resize(items.length * 2);
        }
        items[size++] = item;
//        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        checkNotEmpty();
        final int index = random();
        final Item item = items[index];
        // index == size-1 也没有任何影响
        items[index] = items[size-1];
        items[size-1] = null;
        size--;
        if (size > 0 && size == items.length / 4) {
            resize(items.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        checkNotEmpty();
        return items[random()];
    }

    private void checkNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private void resize(int newSize) {
        final Item[] newArr = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            newArr[i] = items[i];
        }
        items = newArr;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<>();
    }

    private class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private int current = 0;
        private final int[] randomOrder = StdRandom.permutation(size);


        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final Item item = (Item) items[randomOrder[current]];
            current++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void assertNotNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Item item : this) {
            sb.append(item.toString());
        }
        sb.append("]");
        return sb.toString();
    }


    private int random() {
        return StdRandom.uniform(0, size);
    }

    // unit testing (required)
    public static void main(String[] args) {
        final RandomizedQueue<Integer> queue = new RandomizedQueue<>();

        try {
            queue.enqueue(null);
        } catch (IllegalArgumentException e) {
            System.out.println("add null");
        }

        try {
            queue.sample();
        } catch (NoSuchElementException e) {
            System.out.println("sample empty");
        }

        try {
            queue.dequeue();
        } catch (NoSuchElementException e) {
            System.out.println("dequeue empty");
        }

        try {
            Iterator<Integer> iterator = queue.iterator();
            iterator.next();
        } catch (NoSuchElementException e) {
            System.out.println("no more elements");
        }

        try {
            Iterator<Integer> iterator = queue.iterator();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("unsupported");
        }


        queue.enqueue(1);
        System.out.println("add first:" + queue);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.enqueue(4);
        queue.enqueue(5);
        queue.enqueue(6);
        System.out.println("items :" + queue);
        System.out.println("sample :" + queue.sample());
        System.out.println("sample :" + queue.sample());
        System.out.println("sample :" + queue.sample());
        System.out.println("items :" + queue);

        System.out.println("dequeue :" + queue.dequeue());
        System.out.println("items :" + queue);

        System.out.println("dequeue :" + queue.dequeue());
        System.out.println("items :" + queue);

        System.out.println("dequeue :" + queue.dequeue());
        for (int value : queue) {
            System.out.print(value);
        }
        System.out.println();

        for (int value : queue) {
            System.out.print(value);
        }
        System.out.println();
    }

}