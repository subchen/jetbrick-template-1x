/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
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

import java.io.*;
import java.util.zip.ZipFile;

public final class IoUtils {
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    public static byte[] toByteArray(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    public static byte[] toByteArray(InputStream is) {
        UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
        try {
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = is.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    public static char[] toCharArray(File file, String encoding) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), encoding);
            return toCharArray(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
        }
    }

    public static char[] toCharArray(InputStream is, String encoding) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(is, encoding);
            return toCharArray(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
        }
    }

    public static char[] toCharArray(Reader reader) {
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        try {
            char[] buffer = new char[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toCharArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(out);
        }
    }

    public static String toString(File file, String encoding) {
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), encoding);
            return toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(reader);
        }
    }

    public static String toString(InputStream is, String encoding) {
        try {
            InputStreamReader reader = new InputStreamReader(is, encoding);
            return toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(is);
        }
    }

    public static String toString(Reader reader) {
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        try {
            char[] buffer = new char[4096];
            int n = 0;
            while (-1 != (n = reader.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
