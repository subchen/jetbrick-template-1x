/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
