import java.util.*;

@SuppressWarnings("WeakerAccess")
public class SkipList<T extends Comparable<T>> implements SortedSet<T> {
    public Node<T> head;
    public int size;

    public SkipList() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public Comparator<? super T> comparator() {
        return new SkipListComparator<>();
    }


    public static class SkipListComparator<T extends Comparable<T>> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            return Integer.compare(o1.compareTo(o2), 0);
        }
    }


    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
       return new SkipListLimitedView<>(this, fromElement, toElement);
    }


    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SkipListLimitedView<>(this, null, toElement);
    }


    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SkipListLimitedView<>(this, fromElement, null);
    }


    @Override
    public T first() {
        return head.value;
    }


    @Override
    public T last() {
        Node<T> curNode = head;
        while (curNode.addresses.get(0) != null) {
            curNode = curNode.addresses.get(0);
        }

        return curNode.value;
    }


    public static class Node<T> {
        public T value;
        public List<Node<T>> addresses = new ArrayList<>();

        public Node(T value) {
            this.value = value;
        }
    }


    private static int coinFlip() {
        return new Random(System.currentTimeMillis()).nextInt(2);
    }


    public class SkipListIterator implements Iterator<T> {
        Node<T> curNode;

        SkipListIterator() {
            curNode = head;
        }

        @Override
        public boolean hasNext() {
            return curNode.addresses.get(0) != null;
        }

        @Override
        public T next() {
            curNode = curNode.addresses.get(0);
            return curNode.value;
        }
    }


    @Override
    public Iterator<T> iterator() {
        return new SkipListIterator();
    }


    @Override
    public Object[] toArray() {
        if (this.size == 0) { return new Node[0]; }
        else {
            Node<T> curNode = head;
            Node[] result = new Node[this.size];
            result[0] = head;
            int i = 1;


            while (curNode.addresses.get(0) != null) {
                result[i] = curNode.addresses.get(0);
                curNode = curNode.addresses.get(0);
                i++;
            }

            return result;
        }
    }


    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }


    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() {
        return size != 0;
    }


    @Override
    public boolean contains(Object o) {
        if (o instanceof Comparable) {
            if (this.size == 0) return false;

            Node<T> curNode = this.head;
            if (comparator().compare(curNode.value, (T) o) == 0) return true;

            if (!curNode.addresses.isEmpty()) {
                while (curNode.addresses.get(0) != null) {
                    if (curNode.addresses.get(0).value == o) return true;
                    curNode = curNode.addresses.get(0);
                }
            }
        }

        return false;
    }


    private void find (List<Node<T>> traceList, Node<T> curNode, Node<T> node, boolean choice) {
        int i = head.addresses.size() - 1;
        while (i > 0) {
            if (node.value.compareTo(curNode.addresses.get(i).value) < 0) {
                traceList.add(0, curNode);
                curNode = curNode.addresses.get(i - 1);
                i--;
            } else {
                curNode = curNode.addresses.get(i);
            }
        }

        Node<T> lastNode = curNode;
        while (!curNode.addresses.isEmpty() && node.value.compareTo(curNode.addresses.get(0).value) > 0) {
            if (!choice) { lastNode = curNode; }
            curNode = curNode.addresses.get(0);
        }

        if (!choice) { traceList.add(0, lastNode); }
    }


    public boolean add(T t) {
        if (t == null) { throw new NullPointerException(); }
        if (this.contains(t)) { return true; }

        Node<T> node = new Node<>(t);
        if (size == 0) {
            head = node;
        } else {
            if (t.compareTo(head.value) < 0) {
                node.addresses.add(head);
                for (Node<T> address : head.addresses) {
                    if (coinFlip() == 1) {
                        node.addresses.add(head);
                    } else {
                        List<Node<T>> addsList = head.addresses;
                        node.addresses.addAll(addsList.subList(addsList.indexOf(address), addsList.size() - 1));
                        break;
                    }
                }
                head = node;
            } else {
                List<Node<T>> traceList = new ArrayList<>();
                Node<T> curNode = head;

                find(traceList, curNode, node, true);

                if (!curNode.addresses.isEmpty()) { curNode.addresses.set(0, node); }
                else { curNode.addresses.add(node); }

                for (int j = 0; j < traceList.size() - 1; j++) {
                    if (coinFlip() == 1) {
                        node.addresses.add(j + 1, traceList.get(j).addresses.get(j + 1));
                        traceList.get(j).addresses.set(j + 1, node);
                    } else { break; }
                }
            }
        }

        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        if (this.size == 0) { throw new NullPointerException(); }
        if (!this.contains(o)){ return true; }
        else if (size == 1) { head = null; }
        else {
            Node<T> curNode = head;
            List<Node<T>> traceList = new ArrayList<>();

            find(traceList, curNode, new Node<>((T) o), false);

            traceList.get(0).addresses.set(0, curNode.addresses.get(0));


            if (curNode.addresses.size() > 1) {
                for (int i = 1; i < curNode.addresses.size(); i++) {
                    traceList.get(i).addresses.set(i, curNode.addresses.get(i));
                }
            }
        }

        size--;
        return true;
    }


    public void clear() {
        head = null;
        size = 0;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) { return false; }
        }

        return true;
    }


    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            this.add(t);
        }

        return true;
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                this.remove(o);
            }
        }

        return true;
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                this.remove(o);
            }
        }

        return true;
    }
}
