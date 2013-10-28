package jetbrick.template.utils;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * This class provides an API compatible with ByteArrayOutputStream, but with no guarantee of synchronization.
 *
 * Instances of UnsafeByteArrayOutputStream are not safe for use by multiple threads.
 * If such synchronization is required then it is recommended that ByteArrayOutputStream be used.
 */
public class UnsafeByteArrayOutputStream extends OutputStream {
    protected byte[] buffer;
    protected int count;

    public UnsafeByteArrayOutputStream() {
        this(32);
    }

    public UnsafeByteArrayOutputStream(int size) {
        if (size < 0) throw new IllegalArgumentException("Negative initial size: " + size);
        buffer = new byte[size];
    }

    @Override
    public void write(int b) {
        int newcount = count + 1;
        if (newcount > buffer.length) buffer = copyOf(buffer, Math.max(buffer.length << 1, newcount));
        buffer[count] = (byte) b;
        count = newcount;
    }

    @Override
    public void write(byte b[], int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length) || ((off + len) < 0)) throw new IndexOutOfBoundsException();
        if (len == 0) return;
        int newcount = count + len;
        if (newcount > buffer.length) buffer = copyOf(buffer, Math.max(buffer.length << 1, newcount));
        System.arraycopy(b, off, buffer, count, len);
        count = newcount;
    }

    public int size() {
        return count;
    }

    public void reset() {
        count = 0;
    }

    public byte[] toByteArray() {
        return copyOf(buffer, count);
    }

    private static byte[] copyOf(byte[] src, int length) {
        byte[] dest = new byte[length];
        System.arraycopy(src, 0, dest, 0, Math.min(src.length, length));
        return dest;
    }

    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(buffer, 0, count);
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(buffer, 0, count);
    }

    @Override
    public String toString() {
        return new String(buffer, 0, count);
    }

    public String toString(String charset) throws UnsupportedEncodingException {
        return new String(buffer, 0, count, charset);
    }

    @Override
    public void close() throws IOException {
    }
}
