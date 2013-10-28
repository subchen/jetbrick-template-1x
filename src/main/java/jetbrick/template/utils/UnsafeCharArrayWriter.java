package jetbrick.template.utils;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * This class provides an API compatible with CharArrayWriter, but with no guarantee of synchronization.
 *
 * Instances of UnsafeCharArrayWriter are not safe for use by multiple threads.
 * If such synchronization is required then it is recommended that CharArrayWriter be used.
 */
public class UnsafeCharArrayWriter extends Writer {
    protected char buf[];
    protected int count;

    public UnsafeCharArrayWriter() {
        this(32);
    }

    public UnsafeCharArrayWriter(int initialSize) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("Negative initial size: " + initialSize);
        }
        buf = new char[initialSize];
    }

    @Override
    public void write(int c) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        buf[count] = (char) c;
        count = newcount;
    }

    @Override
    public void write(char c[], int off, int len) {
        if ((off < 0) || (off > c.length) || (len < 0) || ((off + len) > c.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        System.arraycopy(c, off, buf, count, len);
        count = newcount;
    }

    @Override
    public void write(String str, int off, int len) {
        int newcount = count + len;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        str.getChars(off, off + len, buf, count);
        count = newcount;
    }

    public void writeTo(Writer out) throws IOException {
        out.write(buf, 0, count);
    }

    @Override
    public UnsafeCharArrayWriter append(CharSequence csq) {
        String s = (csq == null ? "null" : csq.toString());
        write(s, 0, s.length());
        return this;
    }

    @Override
    public UnsafeCharArrayWriter append(CharSequence csq, int start, int end) {
        String s = (csq == null ? "null" : csq).subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    @Override
    public UnsafeCharArrayWriter append(char c) {
        write(c);
        return this;
    }

    public void reset() {
        count = 0;
    }

    public char[] toCharArray() {
        return Arrays.copyOf(buf, count);
    }

    public int size() {
        return count;
    }

    @Override
    public String toString() {
        return new String(buf, 0, count);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
