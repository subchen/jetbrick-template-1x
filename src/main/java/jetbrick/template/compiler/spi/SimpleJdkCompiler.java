package jetbrick.template.compiler.spi;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;
import javax.tools.*;
import jetbrick.template.compiler.JdkCompiler;
import jetbrick.template.compiler.JetClassLoader;
import jetbrick.template.utils.*;

public class SimpleJdkCompiler extends JdkCompiler {
    protected static final String encoding = "utf-8";
    protected final JavaCompiler jc;
    protected final JetClassLoader classloader;

    public SimpleJdkCompiler(JetClassLoader classloader) {
        this.classloader = classloader;
        this.jc = ToolProvider.getSystemJavaCompiler();

        if (jc == null) {
            throw new IllegalStateException("Can't get system java compiler. Please add jdk tools.jar to your classpath.");
        }
    }

    protected void setDefaultClasspath(StandardJavaFileManager standardJavaFileManager) {
        Set<File> files = new HashSet<File>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        while (loader instanceof URLClassLoader && (!"sun.misc.Launcher$AppClassLoader".equals(loader.getClass().getName()))) {
            URLClassLoader urlClassLoader = (URLClassLoader) loader;
            for (URL url : urlClassLoader.getURLs()) {
                files.add(new File(url.getFile()));
            }
            loader = loader.getParent();
        }

        if (files.size() > 0) {
            try {
                Iterable<? extends File> list = standardJavaFileManager.getLocation(StandardLocation.CLASS_PATH);
                for (File file : list) {
                    files.add(file);
                }
                standardJavaFileManager.setLocation(StandardLocation.CLASS_PATH, files);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public Class<?> compile(String qualifiedClassName, String source) {
        try {
            File qualifiedJavaFile = getGenerateJavaSourceFile(qualifiedClassName);
            File qualifiedClassFile = getGenerateJavaClassFile(qualifiedClassName);

            // clean previous generatd files
            qualifiedJavaFile.getParentFile().mkdirs();
            if (qualifiedJavaFile.exists()) {
                qualifiedJavaFile.delete();
            }
            if (qualifiedClassFile.exists()) {
                qualifiedClassFile.delete();
            }

            // write source to file
            OutputStream out = new FileOutputStream(qualifiedJavaFile);
            try {
                out.write(source.getBytes(encoding));
            } finally {
                IoUtils.closeQuietly(out);
            }

            // compile
            generateJavaClass(qualifiedJavaFile, source);

            // load class
            return classloader.loadClass(qualifiedClassName);

        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    @Override
    public File getGenerateJavaSourceFile(String qualifiedClassName) {
        String fileName = qualifiedClassName.replace('.', '/');
        return new File(classloader.getClasspath(), fileName + ".java");
    }

    @Override
    public File getGenerateJavaClassFile(String qualifiedClassName) {
        String fileName = qualifiedClassName.replace('.', '/');
        return new File(classloader.getClasspath(), fileName + ".class");
    }

    protected void generateJavaClass(File qualifiedJavaFile, String source) {
        UnsafeByteArrayOutputStream err = new UnsafeByteArrayOutputStream();
        int result = jc.run(null, null, err, "-encoding", encoding, "-g", "-nowarn", qualifiedJavaFile.getAbsolutePath());

        if (result != 0) {
            try {
                throw new IllegalStateException("Compilation failed.\n" + err.toString(encoding));
            } catch (UnsupportedEncodingException e) {
                throw ExceptionUtils.uncheck(e);
            }
        }
    }
}
