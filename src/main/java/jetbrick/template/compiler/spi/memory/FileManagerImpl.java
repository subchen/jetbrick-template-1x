package jetbrick.template.compiler.spi.memory;

import java.io.IOException;
import javax.tools.*;
import javax.tools.JavaFileObject.Kind;

/**
 * {@link JavaFileManager}的一个实例，用于管理Java源代码和byte code。<br>
 * 所有的源码以{@link CharSequence}的形式保存在内存中，byte code以byte数组形式存放在内存中。
 */
final class FileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
    private final ClassLoaderImpl classLoader;

    FileManagerImpl(JavaFileManager fileManager) {
        super(fileManager);
        this.classLoader = new ClassLoaderImpl(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String qualifiedName, Kind kind, FileObject outputFile) throws IOException {
        JavaFileObjectImpl file = new JavaFileObjectImpl(qualifiedName, kind);
        classLoader.add(qualifiedName, file);
        return file;
    }

    public Class<?> loadClass(String qualifiedClassName) throws ClassNotFoundException {
        return classLoader.loadClass(qualifiedClassName);
    }
}
