package jetbrick.template.compiler.spi.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.tools.JavaFileObject;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.IoUtils;

/** {@link ClassLoader}的一个实现，它map类名和JavaFileObjectImpl的实例。本类在{@link MemoryJdkCompiler}和{@link FileManagerImpl}中被使用。 */
final class ClassLoaderImpl extends ClassLoader {
    private final Map<String, JavaFileObjectImpl> classes = new HashMap<String, JavaFileObjectImpl>();

    ClassLoaderImpl(final ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    protected void add(String qualifiedClassName, JavaFileObjectImpl javaFile) {
        classes.put(qualifiedClassName, javaFile);
    }

    @Override
    protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
        JavaFileObject file = classes.remove(qualifiedClassName);
        if (file != null) {
            InputStream is = null;
            try {
                is = file.openInputStream();
                byte[] bytes = IoUtils.toByteArray(is);
                return defineClass(qualifiedClassName, bytes, 0, bytes.length);
            } catch (IOException e) {
                throw ExceptionUtils.uncheck(e);
            } finally {
                IoUtils.closeQuietly(is);
            }
        }
        // Workaround in Java 6. see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
        try {
            return Class.forName(qualifiedClassName);
        } catch (ClassNotFoundException e) {
            // do nothing
        }
        return super.findClass(qualifiedClassName);
    }
}
