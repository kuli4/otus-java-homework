package pro.kuli4.otus.java.hw02;

import java.util.*;

import pro.kuli4.otus.java.hw02.exceptions.*;

public class DIYArrayList<T> implements List<T> {

    private Object[] arr;
    private int capacity = 8;
    private int appendCurrentPosition = 0;

    @SafeVarargs
    public DIYArrayList(T... initialSet) {
        this.arr = new Object[this.capacity];
        this.addAll(Arrays.asList(initialSet));
    }

    @Override
    public int size() {
        return this.appendCurrentPosition;
    }

    @Override
    public boolean isEmpty() {
        return this.appendCurrentPosition == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        T item;
        try {
            item = (T) o;
        } catch (ClassCastException cce) {
            cce.printStackTrace();
            return false;
        }
        for (Object element : this.arr) {
            if (((T) element).equals(item)) return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYArrayIterator();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(this.arr, this.appendCurrentPosition);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T element) {
        if (this.appendCurrentPosition >= this.capacity) {
            if (!this.extendCapacity()) {
                throw new TooManyElementsException();
            }
        }
        this.arr[this.appendCurrentPosition++] = element;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T item : c) {
            this.add(item);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.capacity = 8;
        this.appendCurrentPosition = 0;
        this.arr = new Object[this.capacity];
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        if (index < this.appendCurrentPosition && index >= 0) {
            return (T) this.arr[index];
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T set(int index, T element) {
        if (index < this.appendCurrentPosition && index >= 0) {
            Object temp = this.arr[index];
            this.arr[index] = element;
            return (T) temp;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < this.appendCurrentPosition; i++) {
            if (Objects.equals(o, this.arr[i])) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYArrayListIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (T element : this) {
            sb.append(element).append(", ");
        }
        return sb.delete(sb.length() - 2, sb.length()).append("]").toString();
    }

    private boolean extendCapacity() {
        if (Integer.MAX_VALUE == this.capacity) {
            return false;
        } else if (this.capacity > Integer.MAX_VALUE / 2) {
            this.capacity = Integer.MAX_VALUE;
        } else {
            this.capacity *= 2;
        }
        this.arr = Arrays.copyOf(this.arr, capacity);
        return true;
    }

    private class DIYArrayIterator implements Iterator<T> {
        private int cursor;

        protected DIYArrayIterator() {
            this(0);
        }

        protected DIYArrayIterator(int startPosition) {
            this.cursor = startPosition;
        }

        @Override
        public boolean hasNext() {
            return !(this.cursor >= appendCurrentPosition);
        }

        @Override
        public T next() {
            if (hasNext()) {
                return get(cursor++);
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    private class DIYArrayListIterator extends DIYArrayIterator implements ListIterator<T> {
        private int cursor;
        private int innerState = -1;

        protected DIYArrayListIterator() {
            this(0);
        }

        protected DIYArrayListIterator(int startPosition) {
            this.cursor = startPosition;
        }

        @Override
        public T next() {
            if (hasNext()) {
                this.innerState = cursor;
                return get(cursor++);
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            return !(this.cursor - 1 < 0);
        }

        @Override
        public T previous() {
            if (hasPrevious()) {
                this.innerState = cursor - 1;
                return get(--cursor);
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            if (this.hasNext()) {
                return this.cursor + 1;
            } else {
                return size();
            }
        }

        @Override
        public int previousIndex() {
            if (this.hasPrevious()) {
                return this.cursor - 1;
            } else {
                return -1;
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T o) {
            if (this.innerState < 0) {
                throw new IllegalStateException();
            }
            DIYArrayList.this.set(innerState, o);
            this.innerState = -1;
        }

        @Override
        public void add(Object o) {
            throw new UnsupportedOperationException();
        }
    }
}
