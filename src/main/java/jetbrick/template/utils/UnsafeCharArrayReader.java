package jetbrick.template.utils;

import java.io.IOException;
import java.io.Reader;

/**
 * This class provides an API compatible with CharArrayReader, but with no guarantee of synchronization.
 *
 * Instances of UnsafeCharArrayReader are not safe for use by multiple threads.
 * If such synchronization is required then it is recommended that CharArrayReader be used.
 */
public class UnsafeCharArrayReader extends Reader {
    protected char buf[];
    protected int pos;
    protected int markedPos = 0;
    protected int count;

    public UnsafeCharArrayReader(char buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    public UnsafeCharArrayReader(char buf[], int offset, int length) {
        if ((offset < 0) || (offset > buf.length) || (length < 0) || ((offset + length) < 0)) {
            throw new IllegalArgumentException();
        }
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
        this.markedPos = offset;
    }

    private void ensureOpen() throws IOException {
        if (buf == null) throw new IOException("Stream closed");
    }

    @Override
    public int read() throws IOException {
        ensureOpen();
        if (pos >= count)
            return -1;
        else
            return buf[pos++];
    }

    @Override
    public int read(char b[], int off, int len) throws IOException {
        ensureOpen();
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        if (pos >= count) {
            return -1;
        }
        if (pos + len > count) {
            len = count - pos;
        }
        if (len <= 0) {
            return 0;
        }
        System.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }

    @Override
    public long skip(long n) throws IOException {
        ensureOpen();
        if (pos + n > count) {
            n = count - pos;
        }
        if (n < 0) {
            return 0;
        }
        pos += n;
        return n;
    }

    @Override
    public boolean ready() throws IOException {
        ensureOpen();
        return (count - pos) > 0;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        ensureOpen();
        markedPos = pos;
    }

    @Override
    public void reset() throws IOException {
        ensureOpen();
        pos = markedPos;
    }

    @Override
    public void close() {
        buf = null;
    }
}
