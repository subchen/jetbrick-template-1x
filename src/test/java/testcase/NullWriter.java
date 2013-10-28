package testcase;

import java.io.Writer;

public class NullWriter extends Writer {

    public static final NullWriter NULL_WRITER = new NullWriter();

    public NullWriter() {
    }

    public void write(int idx) {
    }

    public void write(char[] chr) {
    }

    public void write(char[] chr, int st, int end) {
    }

    public void write(String str) {
    }

    public void write(String str, int st, int end) {
    }

    public void flush() {
    }

    public void close() {
    }

}
