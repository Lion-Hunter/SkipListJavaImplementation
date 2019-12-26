import java.util.*;

public class SkipListLimitedView<T extends Comparable<T>> implements SortedSet<T> {
    private T lowerLimit;
    private T upperLimit;
    private SkipList<T> delegate;

    public SkipListLimitedView(SkipList<T> skipList, T from, T to) {
        this.delegate = skipList;
        this.lowerLimit = from;
        this.upperLimit = to;
    }

    private boolean inBounds(T t) {
        return (lowerLimit == null || t.compareTo(this.lowerLimit) >= 0)
                && (upperLimit == null || t.compareTo(this.upperLimit) <= 0);
    }

    @Override
    public Comparator<? super T> comparator() {
        return new SkipList.SkipListComparator<>();
    }


    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        T newLowerBound = null;
        T newUpperBound = null;

        if (lowerLimit == null) {
            newLowerBound = fromElement;
        } else if (fromElement == null) {
            newLowerBound = lowerLimit;
        } else {
            if (lowerLimit.compareTo(fromElement) > 0) newLowerBound = lowerLimit;
            else newLowerBound = fromElement;
        }
        if (upperLimit == null) {
            newUpperBound = toElement;
        } else if (toElement == null) {
            newLowerBound =upperLimit;
        } else {
            if (upperLimit.compareTo(toElement) < 0) newLowerBound =upperLimit;
            else newLowerBound =toElement;
        }

        return new SkipListLimitedView<T>(delegate, newLowerBound, newUpperBound);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return subSet(null, toElement);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return subSet(fromElement, null);
    }

    @Override
    public T first() {
        for (T t : delegate) {
            if (inBounds(t)) return t;
        }

        throw new NoSuchElementException();
    }

    @Override
    public T last() {
        return  null;
    }

    @Override
    public int size() {
        int counter = 0;

        for (T t : delegate) {
            if (inBounds(t)) { counter++; }
        }

        return counter;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        return (contains(o) && inBounds((T) o));
    }

    @Override
    public Iterator<T> iterator() {
        return new LimitedIterator();
    }

    public class LimitedIterator implements Iterator<T> {
        Iterator<T> iterator;

        public LimitedIterator() {
            this.iterator = delegate.iterator();
        }

        @Override
        public boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T next() {
        return null;
        }
    }

    @Override
    public Object[] toArray() {
        return this.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (inBounds(t)) return delegate.add(t);
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if (inBounds((T) o)) return remove(o);
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
