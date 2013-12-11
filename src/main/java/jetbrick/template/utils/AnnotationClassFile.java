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
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import jetbrick.template.parser.support.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code AnnotationClassFile} reads Java Class File (".class") files and reports the
 * encountered annotations via a simple, developer friendly API.
 * <p>
 * A Java Class File consists of a stream of 8-bit bytes. All 32-bit and 64-bit
 * quantities are constructed by reading in two, four, and eight consecutive 8-bit
 * bytes, respectively. Multi byte data items are always stored in big-endian order,
 * where the high bytes come first. In the Java and Java 2 platforms, this format is
 * supported by interfaces {@link java.io.DataInput} and {@link java.io.DataOutput}.
 * <p>
 * A class file consists of a single ClassFile structure:
 * <pre>
 * ClassFile {
 *   u4 magic;
 *   u2 minor_version;
 *   u2 major_version;
 *   u2 constant_pool_count;
 *   cp_info constant_pool[constant_pool_count-1];
 *   u2 access_flags;
 *   u2 this_class;
 *   u2 super_class;
 *   u2 interfaces_count;
 *   u2 interfaces[interfaces_count];
 *   u2 fields_count;
 *   field_info fields[fields_count];
 *   u2 methods_count;
 *   method_info methods[methods_count];
 *   u2 attributes_count;
 *   attribute_info attributes[attributes_count];
 * }
 *
 * Where:
 * u1 unsigned byte {@link java.io.DataInput#readUnsignedByte()}
 * u2 unsigned short {@link java.io.DataInput#readUnsignedShort()}
 * u4 unsigned int {@link java.io.DataInput#readInt()}
 *
 * Annotations are stored as Attributes (i.e. "RuntimeVisibleAnnotations" and
 * "RuntimeInvisibleAnnotations").
 * </pre>
 * References:
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Java_class_file">Java class file (Wikipedia)</a>
 * (Gentle Introduction);
 * <li><a href="http://download.oracle.com/otndocs/jcp/jcfsu-1.0-fr-eval-oth-JSpec/">Class
 * File Format Specification</a> (Java 6 version) and the
 * <a href="http://java.sun.com/docs/books/jvms/second_edition/html/ClassFile.doc.html">Java
 * VM Specification (Chapter 4)</a> for the real work.
 * </ul>
 * <p>
 *
 * @since 1.1.2
 * @author Guoqiang Chen
 */
public final class AnnotationClassFile {
    private static final Logger log = LoggerFactory.getLogger(AnnotationClassFile.class);

    // Constant Pool type tags
    private static final int CP_UTF8 = 1;
    private static final int CP_INTEGER = 3;
    private static final int CP_FLOAT = 4;
    private static final int CP_LONG = 5;
    private static final int CP_DOUBLE = 6;
    private static final int CP_CLASS = 7;
    private static final int CP_STRING = 8;
    private static final int CP_REF_FIELD = 9;
    private static final int CP_REF_METHOD = 10;
    private static final int CP_REF_INTERFACE = 11;
    private static final int CP_NAME_AND_TYPE = 12;

    // AnnotationElementValue
    private static final int BYTE = 'B';
    private static final int CHAR = 'C';
    private static final int DOUBLE = 'D';
    private static final int FLOAT = 'F';
    private static final int INT = 'I';
    private static final int LONG = 'J';
    private static final int SHORT = 'S';
    private static final int BOOLEAN = 'Z';

    // belows are used for AnnotationElement only
    private static final int OBJECT = 'L';
    private static final int STRING = 's';
    private static final int ENUM = 'e';
    private static final int CLASS = 'c';
    private static final int ANNOTATION = '@';
    private static final int ARRAY = '[';

    private final AnnotationFilter annotationFilter;

    // Reusing the constantPool is not needed for better performance
    private Object[] constantPool;

    // the 'raw' name of this interface or class (using '/' instead of '.' in package name)
    @SuppressWarnings("unused")
    private String rawTypeName;

    // method or filed name
    @SuppressWarnings("unused")
    private String memberName;
    @SuppressWarnings("unused")
    private String memberDescriptor;

    public AnnotationClassFile(AnnotationFilter annotationFilter) {
        this.annotationFilter = annotationFilter;
    }

    public boolean isAnnotationed(File file) {
        try {
            return isAnnotationed(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public boolean isAnnotationed(JarFile jar, JarEntry entry) {
        try {
            return isAnnotationed(jar.getInputStream(entry));
        } catch (IOException e) {
            log.warn("Ignored IOException in load class file in jar file.", e);
            return false;
        }
    }

    public boolean isAnnotationed(InputStream classInputStream) {
        try {
            ClassFileDataInput input = new ClassFileDataInput(classInputStream);
            if (hasCafebabe(input) && greatThanJdk15(input)) {
                readClassFile(input);
            }
        } catch (AnnotationFoundException e) {
            return true;
        } catch (AnnotationNotFoundException e) {
            return false;
        } catch (Throwable e) {
            log.warn("Ignored UnknownException in parsing class file.", e);
            return false;
        } finally {
            IoUtils.closeQuietly(classInputStream);
        }
        return false;
    }

    private boolean hasCafebabe(final ClassFileDataInput di) throws IOException {
        return di.size() > 4 && di.readInt() == 0xCAFEBABE;
    }

    private boolean greatThanJdk15(final ClassFileDataInput di) throws IOException {
        // sequence: minor version, major version (argument_index is 1-based)
        @SuppressWarnings("unused")
        int minor = di.readUnsignedShort();
        int major = di.readUnsignedShort();

        return major >= 49;
    }

    /**
     * Inspect the given (Java) class file in streaming mode.
     */
    private void readClassFile(final DataInput di) throws IOException {
        readConstantPoolEntries(di);
        readAccessFlags(di);
        readThisClass(di);
        readSuperClass(di);
        readInterfaces(di);
        readFields(di);
        readMethods(di);
        readAttributes(di, ElementType.TYPE);
    }

    private void readConstantPoolEntries(final DataInput di) throws IOException {
        final int count = di.readUnsignedShort();
        constantPool = new Object[count];
        for (int i = 1; i < count; ++i) {
            if (readConstantPoolEntry(di, i)) {
                // double slot
                ++i;
            }
        }
    }

    /**
     * Return true if a double slot is read (in case of Double or Long constant).
     */
    private boolean readConstantPoolEntry(final DataInput di, final int index) throws IOException {
        final int tag = di.readUnsignedByte();
        switch (tag) {
        case CP_UTF8:
            constantPool[index] = di.readUTF();
            return false;
        case CP_INTEGER:
            di.skipBytes(4); // readInt()
            return false;
        case CP_FLOAT:
            di.skipBytes(4); // readFloat()
            return false;
        case CP_LONG:
            di.skipBytes(8); // readLong()
            return true;
        case CP_DOUBLE:
            di.skipBytes(8); // readDouble()
            return true;
        case CP_CLASS:
        case CP_STRING:
            // reference to CP_UTF8 entry. The referenced index can have a higher number!
            constantPool[index] = di.readUnsignedShort();
            return false;
        case CP_REF_FIELD:
        case CP_REF_METHOD:
        case CP_REF_INTERFACE:
        case CP_NAME_AND_TYPE:
            di.skipBytes(4); // readUnsignedShort() * 2
            return false;
        default:
            throw new ClassFormatError("Unkown tag value for constant pool entry: " + tag);
        }
    }

    private void readAccessFlags(final DataInput di) throws IOException {
        int flags = di.readUnsignedShort(); // u2
        if (!Modifier.isPublic(flags)) {
            throw new AnnotationNotFoundException();
        }
        if (Modifier.isInterface(flags) || Modifier.isAbstract(flags)) {
            throw new AnnotationNotFoundException();
        }
    }

    private void readThisClass(final DataInput di) throws IOException {
        rawTypeName = resolveUtf8(di);
    }

    private void readSuperClass(final DataInput di) throws IOException {
        di.skipBytes(2); // u2
    }

    private void readInterfaces(final DataInput di) throws IOException {
        final int count = di.readUnsignedShort();
        di.skipBytes(count * 2); // count * u2
    }

    private void readFields(final DataInput di) throws IOException {
        final int count = di.readUnsignedShort();
        for (int i = 0; i < count; ++i) {
            readAccessFlags(di);
            memberName = resolveUtf8(di);
            memberDescriptor = resolveUtf8(di);
            readAttributes(di, ElementType.FIELD);
        }
    }

    private void readMethods(final DataInput di) throws IOException {
        final int count = di.readUnsignedShort();
        for (int i = 0; i < count; ++i) {
            readAccessFlags(di);
            memberName = resolveUtf8(di);
            memberDescriptor = resolveUtf8(di);
            readAttributes(di, ElementType.METHOD);
        }
    }

    // It is a unused method in this class, but it is useful in other side.
    // So I don't delete it and only change to 'proteted' to avoid warning.
    protected Class<?>[] getMethodParameterTypes(String methodDescriptor) {
        int ipos = methodDescriptor.indexOf('(');
        int jpos = methodDescriptor.indexOf(')');
        String parameters = methodDescriptor.substring(ipos + 1, jpos);
        if (parameters.length() == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }

        String[] types = parameters.split(";");
        Class<?>[] parameterTypes = new Class<?>[types.length];
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            if (type.length() > 0) {
                switch (type.charAt(0)) {
                case BOOLEAN:
                    parameterTypes[i] = Boolean.TYPE;
                    break;
                case CHAR:
                    parameterTypes[i] = Character.TYPE;
                    break;
                case BYTE:
                    parameterTypes[i] = Byte.TYPE;
                    break;
                case SHORT:
                    parameterTypes[i] = Short.TYPE;
                    break;
                case INT:
                    parameterTypes[i] = Integer.TYPE;
                    break;
                case LONG:
                    parameterTypes[i] = Long.TYPE;
                    break;
                case FLOAT:
                    parameterTypes[i] = Float.TYPE;
                    break;
                case DOUBLE:
                    parameterTypes[i] = Double.TYPE;
                    break;
                case OBJECT:
                    type = type.substring(1).replace('/', '.');
                    parameterTypes[i] = ClassUtils.getClass(type);
                    break;
                default:
                    throw new AssertionError("parameter type =" + type);
                }
            }
        }
        return parameterTypes;
    }

    private void readAttributes(DataInput di, ElementType type) throws IOException {
        final int count = di.readUnsignedShort();

        for (int i = 0; i < count; ++i) {
            final String name = resolveUtf8(di);
            // in bytes, use this to skip the attribute info block
            final int length = di.readInt();

            if (annotationFilter.accept(type) && ("RuntimeVisibleAnnotations".equals(name) || "RuntimeInvisibleAnnotations".equals(name))) {
                readAnnotations(di, type);
            } else {
                di.skipBytes(length);
            }
        }
    }

    private void readAnnotations(DataInput di, ElementType type) throws IOException {
        // the number of Runtime(In)VisibleAnnotations
        final int count = di.readUnsignedShort();

        boolean found = false;
        for (int i = 0; i < count; ++i) {
            String annotation = readAnnotation(di);
            switch (type) {
            case TYPE:
                found = annotationFilter.typeAnnotationMap.containsKey(annotation);
                break;
            case FIELD:
                found = annotationFilter.fieldAnnotationMap.containsKey(annotation);
                break;
            case METHOD:
                found = annotationFilter.methodAnnotationMap.containsKey(annotation);
                break;
            default:
                throw new AssertionError("annotation type =" + type);
            }
            if (found) {
                // 用一个 Expcetion 简单处理跳出业务逻辑
                throw new AnnotationFoundException();
            }
        }
    }

    private String readAnnotation(final DataInput di) throws IOException {
        final String annotation = resolveUtf8(di);
        // num_element_value_pairs
        final int count = di.readUnsignedShort();

        for (int i = 0; i < count; ++i) {
            di.skipBytes(2);
            readAnnotationElementValue(di);
        }
        return annotation;
    }

    private void readAnnotationElementValue(final DataInput di) throws IOException {
        final int tag = di.readUnsignedByte();
        switch (tag) {
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case BOOLEAN:
        case STRING:
            di.skipBytes(2);
            break;
        case ENUM:
            di.skipBytes(4); // 2 * u2
            break;
        case CLASS:
            di.skipBytes(2);
            break;
        case ANNOTATION:
            readAnnotation(di);
            break;
        case ARRAY:
            final int count = di.readUnsignedShort();
            for (int i = 0; i < count; ++i) {
                readAnnotationElementValue(di);
            }
            break;
        default:
            throw new ClassFormatError("Not a valid annotation element type tag: 0x" + Integer.toHexString(tag));
        }
    }

    /**
     * Look up the String value, identified by the u2 index value from constant pool
     * (direct or indirect).
     */
    private String resolveUtf8(final DataInput di) throws IOException {
        final int index = di.readUnsignedShort();
        final Object value = constantPool[index];
        final String s;
        if (value instanceof Integer) {
            s = (String) constantPool[(Integer) value];
        } else {
            s = (String) value;
        }
        return s;
    }

    static class ClassFileDataInput implements DataInput {
        private byte[] buffer;
        private int size; // the number of significant bytes read
        private int pointer; // the "read pointer"

        ClassFileDataInput(InputStream is) throws IOException {
            this.buffer = new byte[8 * 1024]; // default 8K cache
            load(is);
        }

        /**
         * Clear and fill the buffer of this {@code ClassFileBuffer} with the
         * supplied byte stream.
         * The read pointer is reset to the start of the byte array.
         */
        private void load(final InputStream in) throws IOException {
            pointer = 0;
            size = 0;
            int n;
            do {
                n = in.read(buffer, size, buffer.length - size);
                if (n > 0) {
                    size += n;
                }
                ensureCapacity();
            } while (n >= 0);
        }

        /**
         * Sets the file-pointer offset, measured from the beginning of this file,
         * at which the next read or write occurs.
         */
        public void seek(final int position) throws IOException {
            if (position < 0) {
                throw new IllegalArgumentException("position < 0: " + position);
            }
            if (position > size) {
                throw new EOFException();
            }
            this.pointer = position;
        }

        /**
         * Return the size (in bytes) of this Java ClassFile file.
         */
        public int size() {
            return size;
        }

        @Override
        public void readFully(final byte[] bytes) throws IOException {
            readFully(bytes, 0, bytes.length);
        }

        @Override
        public void readFully(final byte[] bytes, final int offset, final int length) throws IOException {
            if (length < 0 || offset < 0 || offset + length > bytes.length) {
                throw new IndexOutOfBoundsException();
            }
            if (pointer + length > size) {
                throw new EOFException();
            }
            System.arraycopy(buffer, pointer, bytes, offset, length);
            pointer += length;
        }

        @Override
        public int skipBytes(final int n) throws IOException {
            seek(pointer + n);
            return n;
        }

        @Override
        public byte readByte() throws IOException {
            if (pointer >= size) {
                throw new EOFException();
            }
            return buffer[pointer++];
        }

        @Override
        public boolean readBoolean() throws IOException {
            return readByte() != 0;
        }

        @Override
        public int readUnsignedByte() throws IOException {
            if (pointer >= size) {
                throw new EOFException();
            }
            return read();
        }

        @Override
        public int readUnsignedShort() throws IOException {
            if (pointer + 2 > size) {
                throw new EOFException();
            }
            return (read() << 8) + read();
        }

        @Override
        public short readShort() throws IOException {
            return (short) readUnsignedShort();
        }

        @Override
        public char readChar() throws IOException {
            return (char) readUnsignedShort();
        }

        @Override
        public int readInt() throws IOException {
            if (pointer + 4 > size) {
                throw new EOFException();
            }
            //@formatter:off
            return (read() << 24) +
                   (read() << 16) +
                   (read() << 8) +
                    read();
           //@formatter:on
        }

        @Override
        public long readLong() throws IOException {
            if (pointer + 8 > size) {
                throw new EOFException();
            }
            //@formatter:off
            return ((long)read() << 56) +
                   ((long)read() << 48) +
                   ((long)read() << 40) +
                   ((long)read() << 32) +
                         (read() << 24) +
                         (read() << 16) +
                         (read() << 8)  +
                          read();
            //@formatter:on
        }

        @Override
        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        @Override
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }

        @Override
        @Deprecated
        public String readLine() throws IOException {
            throw new UnsupportedOperationException("readLine() is deprecated and not supported");
        }

        @Override
        public String readUTF() throws IOException {
            return DataInputStream.readUTF(this);
        }

        private int read() {
            return buffer[pointer++] & 0xff;
        }

        private void ensureCapacity() {
            if (size >= buffer.length) {
                final byte[] newBuffer = new byte[buffer.length * 2];
                System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
                buffer = newBuffer;
            }
        }
    }

    private static class AnnotationFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    private static class AnnotationNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public static class AnnotationFilter {
        private Map<String, Class<? extends Annotation>> typeAnnotationMap;
        private Map<String, Class<? extends Annotation>> methodAnnotationMap;
        private Map<String, Class<? extends Annotation>> fieldAnnotationMap;

        public void addTypeAnnotation(Class<? extends Annotation> annoClass) {
            if (typeAnnotationMap == null) {
                typeAnnotationMap = new HashMap<String, Class<? extends Annotation>>();
            }
            typeAnnotationMap.put("L" + annoClass.getName().replace('.', '/') + ";", annoClass);
        }

        public void addMethodAnnotation(Class<? extends Annotation> annoClass) {
            if (methodAnnotationMap == null) {
                methodAnnotationMap = new HashMap<String, Class<? extends Annotation>>();
            }
            methodAnnotationMap.put("L" + annoClass.getName().replace('.', '/') + ";", annoClass);
        }

        public void addFieldAnnotation(Class<? extends Annotation> annoClass) {
            if (fieldAnnotationMap == null) {
                fieldAnnotationMap = new HashMap<String, Class<? extends Annotation>>();
            }
            fieldAnnotationMap.put("L" + annoClass.getName().replace('.', '/') + ";", annoClass);
        }

        protected boolean accept(ElementType type) {
            switch (type) {
            case TYPE:
                return typeAnnotationMap != null;
            case METHOD:
                return methodAnnotationMap != null;
            case FIELD:
                return fieldAnnotationMap != null;
            default:
                return false;
            }
        }
    }
}
