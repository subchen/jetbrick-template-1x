package jetbrick.template.compiler.jdk;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;

/**
 * 编译{@link CharSequence}形式的源码，返回一个Class。<br>
 */
public class JdkCompiler {
    private final JavaCompiler compiler;
    private final FileManagerImpl javaFileManager;
    private final List<String> options; // 编译参数

    public JdkCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        if (this.compiler == null) {
            throw new IllegalStateException("Can't get system java compiler. Please add jdk tools.jar to your classpath.");
        }
        this.options = new ArrayList<String>();

        StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(null, null, null);
        setDefaultClasspath(standardJavaFileManager);
        this.javaFileManager = new FileManagerImpl(standardJavaFileManager);
    }

    // 加入 javac 参数
    public void addOption(String option) {
        options.add(option);
    }

    private void setDefaultClasspath(StandardJavaFileManager standardJavaFileManager) {
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

    // 非线程安全方法
    public Class<?> compile(String qualifiedClassName, CharSequence source) {
        // 准备待编译文件
        int pos = qualifiedClassName.lastIndexOf('.');
        String className = pos == -1 ? qualifiedClassName : qualifiedClassName.substring(pos + 1);
        JavaFileObjectImpl fileObject = new JavaFileObjectImpl(className, source);

        // 编译代码
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>(); // 编译器编译中的诊断信息
        CompilationTask task = compiler.getTask(null, javaFileManager, diagnostics, options, null, Arrays.asList(fileObject));
        Boolean result = task.call();

        // 返回编译结果
        if ((result == null) || !result.booleanValue()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Compilation failed.");
            sb.append("\nclass: " + qualifiedClassName);
            sb.append("\n");
            for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
                //sb.append("\nline: " + d.getLineNumber());
                sb.append("\n" + d.getMessage(Locale.ENGLISH));
                sb.append("\n");
            }
            throw new IllegalStateException(sb.toString());
        }
        try {
            return javaFileManager.loadClass(qualifiedClassName);
        } catch (Throwable ex) {
            throw new IllegalStateException("loadClass failed. class: " + qualifiedClassName, ex);
        }
    }
}
