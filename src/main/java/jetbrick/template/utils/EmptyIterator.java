package jetbrick.template.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EmptyIterator implements Iterator<Object> {
    public static final Iterator<?> INSTANCE = new EmptyIterator();

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
