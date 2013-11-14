package jetbrick.template.compiler.spi.memory;

import java.util.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import jetbrick.template.compiler.JetClassLoader;
import jetbrick.template.compiler.spi.SimpleJdkCompiler;
import jetbrick.template.utils.StringUtils;

/**
 * 编译{@link CharSequence}形式的源码，返回一个Class。<br>
 */
public class MemoryJdkCompiler extends SimpleJdkCompiler {
    private final FileManagerImpl fileManager;
    private final List<String> options; // 编译参数

    public MemoryJdkCompiler(JetClassLoader classloader) {
        super(classloader);

        this.options = Arrays.asList("-encoding", encoding, "-g", "-nowarn");

        StandardJavaFileManager standardJavaFileManager = jc.getStandardFileManager(null, null, null);
        setDefaultClasspath(standardJavaFileManager);
        this.fileManager = new FileManagerImpl(standardJavaFileManager);
    }

    @Override
    public Class<?> compile(String qualifiedClassName, String source) {
        // 准备待编译文件
        int pos = qualifiedClassName.lastIndexOf('.');
        String className = pos == -1 ? qualifiedClassName : qualifiedClassName.substring(pos + 1);
        JavaFileObjectImpl fileObject = new JavaFileObjectImpl(className, source);

        // 编译代码
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>(); // 编译器编译中的诊断信息
        CompilationTask task = jc.getTask(null, fileManager, diagnostics, options, null, Arrays.asList(fileObject));
        Boolean result = task.call();

        // 返回编译结果
        if ((result == null) || !result.booleanValue()) {
            String[] sourceLines = source.split("\r?\n", -1);
            StringBuilder sb = new StringBuilder();
            sb.append("Compilation failed.");
            sb.append('\n');
            for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
                sb.append(d.getMessage(Locale.ENGLISH)).append('\n');
                sb.append(StringUtils.getPrettyError(sourceLines, (int) d.getLineNumber(), (int) d.getColumnNumber(), (int) d.getPosition(), (int) d.getPosition(), 3));
            }
            sb.append(diagnostics.getDiagnostics().size());
            sb.append(" error(s)\n");
            throw new IllegalStateException(sb.toString());
        }

        try {
            return fileManager.loadClass(qualifiedClassName);
        } catch (Throwable ex) {
            throw new IllegalStateException("loadClass failed. class: " + qualifiedClassName, ex);
        }
    }
}
