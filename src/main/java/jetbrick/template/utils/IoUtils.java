package jetbrick.template.utils;

import java.io.*;
import java.util.zip.ZipFile;

public final class IoUtils {

    public static byte[] toByteArray(InputStream is) {
        UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = is.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            closeQuietly(out);
        }
    }

    public static char[] toCharArray(File file, String encoding) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return toCharArray(is, encoding);
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            closeQuietly(is);
        }
    }

    public static char[] toCharArray(InputStream is, String encoding) {
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        try {
            InputStreamReader in = new InputStreamReader(is, encoding);
            char[] buffer = new char[4096];
            int n = 0;
            while (-1 != (n = in.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toCharArray();
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            closeQuietly(out);
        }
    }

    public static void closeQuietly(Closeable obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static void closeQuietly(ZipFile obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
