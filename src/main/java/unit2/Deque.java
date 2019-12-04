package unit2;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class Deque<Item> implements Iterable<Item> {
    private int size = 0;
    private Node first;
    private Node last;

    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    private Node createNode(Item item) {
        final Node node = new Node();
        node.item = item;
        node.next = null;
        node.previous = null;
        return node;
    }

    // add the item to the front
    public void addFirst(Item item) {
        assertNotNull(item);
        final Node node = createNode(item);
        if (first == null) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.previous = node;
            first = node;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        assertNotNull(item);
        final Node node = createNode(item);
        if (last == null) {
            first = node;
            last = node;
        } else {
            last.next = node;
            node.previous = last;
            last = node;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        assertNotZero("no first element");
        if (size == 1) {
            final Node node = first;
            first = null;
            last = null;
            size--;
            return node.item;
        } else {
            final Node removed = first;
            final Node node = first.next;
            node.previous = null;
            first = node;
            size--;
            return removed.item;
        }
    }

    // remove and return the item from the back
    public Item removeLast() {
        assertNotZero("no last element");
        if (size == 1) {
            final Node node = last;
            first = null;
            last = null;
            size--;
            return node.item;
        } else {
            final Node removed = last;
            final Node node = last.previous;
            node.next = null;
            last = node;
            size--;
            return removed.item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator<>();
    }

    private class DequeIterator<Item> implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            if (current == null) {
                return false;
            }
            return true;
        }

        @Override
        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("no more items");
            }
            final Item item = (Item) current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove is not supported");
        }

        @Override
        public void forEachRemaining(Consumer<? super Item> action) {

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

    private void assertNotNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
    }

    private void assertNotZero(String msg) {
        if (size == 0) {
            throw new NoSuchElementException(msg);
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        final Deque<Integer> deque = new Deque<>();
        try {
            deque.addFirst(null);
        } catch (IllegalArgumentException e) {
            System.out.println("add first null");
        }

        try {
            deque.addLast(null);
        } catch (IllegalArgumentException e) {
            System.out.println("add last null");
        }

        try {
            deque.removeFirst();
        } catch (NoSuchElementException e) {
            System.out.println("remove first null");
        }

        try {
            deque.removeLast();
        } catch (NoSuchElementException e) {
            System.out.println("remove last null");
        }

        try {
            Iterator<Integer> iterator = deque.iterator();
            iterator.next();
        } catch (NoSuchElementException e) {
            System.out.println("no more elements");
        }

        try {
            Iterator<Integer> iterator = deque.iterator();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            System.out.println("unsupported");
        }


        deque.addFirst(1);
        System.out.println("add first:" + deque);
        deque.addLast(2);
        System.out.println("add last:" + deque);
        deque.addLast(3);
        deque.removeFirst();
        System.out.println("removeFirst:" + deque);
        deque.removeLast();
        System.out.println("removeLast:" + deque);
        deque.addFirst(1);
        deque.removeFirst();
        deque.addFirst(3);
        deque.removeFirst();

    }

}