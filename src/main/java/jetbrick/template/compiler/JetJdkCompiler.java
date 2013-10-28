package jetbrick.template.compiler;

import java.io.*;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import jetbrick.template.utils.*;

public class JetJdkCompiler {
    private static final String encoding = "utf-8";

    private final JavaCompiler jc;
    private final JetClassLoader classloader;
    private final String path;

    public JetJdkCompiler(String path, JetClassLoader classloader) {
        this.path = path;
        this.classloader = classloader;
        this.jc = ToolProvider.getSystemJavaCompiler();
    }

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
            generateJavaClass(qualifiedJavaFile);
            // load class
            return classloader.loadClass(qualifiedClassName);

        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public File getGenerateJavaSourceFile(String qualifiedClassName) {
        String fileName = qualifiedClassName.replace('.', '/');
        return new File(path, fileName + ".java");
    }

    public File getGenerateJavaClassFile(String qualifiedClassName) {
        String fileName = qualifiedClassName.replace('.', '/');
        return new File(path, fileName + ".class");
    }

    private void generateJavaClass(File qualifiedJavaFile) {
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
