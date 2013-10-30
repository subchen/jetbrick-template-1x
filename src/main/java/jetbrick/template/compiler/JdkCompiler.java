package jetbrick.template.compiler;

import java.io.File;
import jetbrick.template.compiler.spi.AdvanceJdkCompiler;

public abstract class JdkCompiler {

    public static JdkCompiler create(JetClassLoader classloader) {
        return new AdvanceJdkCompiler(classloader);
    }

    public abstract Class<?> compile(String qualifiedClassName, String source);

    public abstract File getGenerateJavaSourceFile(String qualifiedClassName);

    public abstract File getGenerateJavaClassFile(String qualifiedClassName);
}
