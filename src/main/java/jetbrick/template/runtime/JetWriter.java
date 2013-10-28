package jetbrick.template.runtime;

import java.io.*;

public abstract class JetWriter {

    public static JetWriter create(Writer os, String encoding) {
        return new JetWriterImpl(os, encoding);
    }

    public static JetWriter create(OutputStream os, String encoding) {
        return new JetOutputStreamImpl(os, encoding);
    }

    public abstract void print(String text, byte[] bytes) throws IOException;

    public void print(boolean x) throws IOException {
        print(x ? "true" : "false");
    }

    public void print(byte x) throws IOException {
        print(String.valueOf(x));
    }

    public void print(char x) throws IOException {
        print(String.valueOf(x));
    }

    public void print(short x) throws IOException {
        print(String.valueOf(x));
    }

    public void print(int x) throws IOException {
        print(String.valueOf(x));
    }

    public void print(long x) throws IOException {
        print(String.valueOf(x));
    }

    public void print(float x) throws IOException {
        print(String.valueOf(x));
    }

    public void print(double x) throws IOException {
        print(String.valueOf(x));
    }

    public abstract void print(byte x[]) throws IOException;

    public abstract void print(char x[]) throws IOException;

    public abstract void print(CharSequence x) throws IOException;

    public void print(Object x) throws IOException {
        if (x != null) {
            print(x.toString());
        }
    }

    public abstract void println() throws IOException;

    public void println(boolean x) throws IOException {
        print(x);
        println();
    }

    public void println(byte x) throws IOException {
        print(x);
        println();
    }

    public void println(char x) throws IOException {
        print(x);
        println();
    }

    public void println(short x) throws IOException {
        print(x);
        println();
    }

    public void println(int x) throws IOException {
        print(x);
        println();
    }

    public void println(long x) throws IOException {
        print(x);
        println();
    }

    public void println(float x) throws IOException {
        print(x);
        println();
    }

    public void println(double x) throws IOException {
        print(x);
        println();
    }

    public void println(byte x[]) throws IOException {
        if (x != null) {
            print(x);
            println();
        }
    }

    public void println(char x[]) throws IOException {
        if (x != null) {
            print(x);
            println();
        }
    }

    public void println(CharSequence x) throws IOException {
        if (x != null) {
            print(x);
            println();
        }
    }

    public void println(Object x) throws IOException {
        if (x != null) {
            print(x.toString());
            println();
        }
    }

    public abstract void flush() throws IOException;

    public abstract void close() throws IOException;

    static class JetWriterImpl extends JetWriter {
        private static final String NEWLINE = "\r\n";
        private final Writer os;
        private final String encoding;

        public JetWriterImpl(Writer os, String encoding) {
            this.os = os;
            this.encoding = encoding;
        }

        @Override
        public void print(String text, byte[] bytes) throws IOException {
            os.write(text);
        }

        @Override
        public void print(byte x[]) throws IOException {
            if (x != null) {
                os.write(new String(x, encoding));
            }
        }

        @Override
        public void print(char x[]) throws IOException {
            if (x != null) {
                os.write(x);
            }
        }

        @Override
        public void print(CharSequence x) throws IOException {
            if (x != null) {
                os.write(x.toString());
            }
        }

        @Override
        public void println() throws IOException {
            os.write(NEWLINE);
        }

        @Override
        public void flush() throws IOException {
            os.flush();
        }

        @Override
        public void close() throws IOException {
            os.close();
        }
    }

    static class JetOutputStreamImpl extends JetWriter {
        private static final byte[] NEWLINE = new byte[] { '\r', '\n' };
        private final OutputStream os;
        private final String encoding;

        public JetOutputStreamImpl(OutputStream os, String encoding) {
            this.os = os;
            this.encoding = encoding;
        }

        @Override
        public void print(String text, byte[] bytes) throws IOException {
            os.write(bytes);
        }

        @Override
        public void print(byte x[]) throws IOException {
            if (x != null) {
                os.write(x);
            }
        }

        @Override
        public void print(char x[]) throws IOException {
            if (x != null) {
                os.write(new String(x).getBytes(encoding));
            }
        }

        @Override
        public void print(CharSequence x) throws IOException {
            if (x != null) {
                os.write(x.toString().getBytes(encoding));
            }
        }

        @Override
        public void println() throws IOException {
            os.write(NEWLINE);
        }

        @Override
        public void flush() throws IOException {
            os.flush();
        }

        @Override
        public void close() throws IOException {
            os.close();
        }
    }
}
