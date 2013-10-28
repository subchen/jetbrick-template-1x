package jetbrick.template.utils;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator implements Iterator<Object> {
    protected Object array;
    protected int startIndex = 0;
    protected int endIndex = 0;
    protected int index = 0;

    public ArrayIterator(Object array) {
        setArray(array);
    }

    public ArrayIterator(Object array, int startIndex) {
        setArray(array);
        checkBound(startIndex, "start");
        this.startIndex = startIndex;
        this.index = startIndex;
    }

    public ArrayIterator(Object array, int startIndex, int endIndex) {
        setArray(array);
        checkBound(startIndex, "start");
        checkBound(endIndex, "end");
        if (endIndex < startIndex) {
            throw new IllegalArgumentException("End index must not be less than start index.");
        }
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.index = startIndex;
    }

    protected void checkBound(int bound, String type) {
        if (bound > this.endIndex) {
            throw new ArrayIndexOutOfBoundsException("Attempt to make an ArrayIterator that " + type + "s beyond the end of the array. ");
        }
        if (bound < 0) throw new ArrayIndexOutOfBoundsException("Attempt to make an ArrayIterator that " + type + "s before the start of the array. ");
    }

    @Override
    public boolean hasNext() {
        return (this.index < this.endIndex);
    }

    @Override
    public Object next() {
        if (!(hasNext())) {
            throw new NoSuchElementException();
        }
        return Array.get(this.array, this.index++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported.");
    }

    public Object getArray() {
        return this.array;
    }

    private void setArray(Object array) {
        this.endIndex = Array.getLength(array);
        this.startIndex = 0;
        this.array = array;
        this.index = 0;
    }

    public void reset() {
        this.index = this.startIndex;
    }
}
