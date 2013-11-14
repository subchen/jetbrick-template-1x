package jetbrick.template.compiler.spi;

import java.io.File;
import java.util.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import jetbrick.template.compiler.JetClassLoader;
import jetbrick.template.utils.StringUtils;

/**
 * 比 SimpleJdkCompiler 提供更详细的错误输出 
 */
public class AdvanceJdkCompiler extends SimpleJdkCompiler {
    private final StandardJavaFileManager fileManager;
    private final List<String> options; // 编译参数

    public AdvanceJdkCompiler(JetClassLoader classloader) {
        super(classloader);

        this.fileManager = jc.getStandardFileManager(null, null, null);
        this.options = Arrays.asList("-encoding", encoding, "-g", "-nowarn");

        setDefaultClasspath(fileManager);
    }

    @Override
    protected void generateJavaClass(File qualifiedJavaFile, String source) {
        // 编译代码
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>(); // 编译器编译中的诊断信息
        Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjects(qualifiedJavaFile); // 要编译的所有Java文件
        CompilationTask task = jc.getTask(null, fileManager, diagnostics, options, null, files);
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
    }
}
