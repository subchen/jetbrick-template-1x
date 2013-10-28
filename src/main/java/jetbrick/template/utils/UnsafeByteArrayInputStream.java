package jetbrick.template.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides an API compatible with ByteArrayInputStream, but with no guarantee of synchronization.
 *
 * Instances of UnsafeByteArrayInputStream are not safe for use by multiple threads.
 * If such synchronization is required then it is recommended that ByteArrayInputStream be used.
 */
public class UnsafeByteArrayInputStream extends InputStream {
    protected byte data[];
    protected int position, limit, mark = 0;

    public UnsafeByteArrayInputStream(byte buf[]) {
        this(buf, 0, buf.length);
    }

    public UnsafeByteArrayInputStream(byte buf[], int offset) {
        this(buf, offset, buf.length - offset);
    }

    public UnsafeByteArrayInputStream(byte buf[], int offset, int length) {
        data = buf;
        position = mark = offset;
        limit = Math.min(offset + length, buf.length);
    }

    @Override
    public int read() {
        return (position < limit) ? (data[position++] & 0xff) : -1;
    }

    @Override
    public int read(byte b[], int off, int len) {
        if (b == null) throw new NullPointerException("read byte[] == null");
        if (off < 0 || len < 0 || len > b.length - off) throw new IndexOutOfBoundsException();
        if (position >= limit) return -1;
        if (position + len > limit) len = limit - position;
        if (len <= 0) return 0;
        System.arraycopy(data, position, b, off, len);
        position += len;
        return len;
    }

    @Override
    public long skip(long len) {
        if (position + len > limit) len = limit - position;
        if (len <= 0) return 0;
        position += len;
        return len;
    }

    @Override
    public int available() {
        return limit - position;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) {
        mark = position;
    }

    @Override
    public void reset() {
        position = mark;
    }

    @Override
    public void close() throws IOException {
    }

    public int position() {
        return position;
    }

    public void position(int newPosition) {
        position = newPosition;
    }

    public int size() {
        return data == null ? 0 : data.length;
    }
}
