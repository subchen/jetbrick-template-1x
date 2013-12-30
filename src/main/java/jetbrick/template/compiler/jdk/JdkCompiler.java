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
package jetbrick.template.compiler.jdk;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import jetbrick.template.compiler.*;
import jetbrick.template.compiler.JavaCompiler;
import jetbrick.template.utils.*;

/**
 * 利用 JDK6 提供的 Java Compiler 进行编译，提供详细的错误输出。
 */
public class JdkCompiler extends JavaCompiler {
    private static final String encoding = "utf-8"; // java source encoding
    private final javax.tools.JavaCompiler jc;
    private final StandardJavaFileManager fileManager;
    private final List<String> options; // 编译参数

    public JdkCompiler(JetTemplateClassLoader classloader) {
        super(classloader);

        javax.tools.JavaCompiler jcc = ToolProvider.getSystemJavaCompiler();
        if (jcc == null) {
            // JDT 支持 ServiceLoader 方式载入。
            ServiceLoader<javax.tools.JavaCompiler> serviceLoader = ServiceLoader.load(javax.tools.JavaCompiler.class);
            Iterator<javax.tools.JavaCompiler> iterator = serviceLoader.iterator();
            if (iterator.hasNext()) {
                jcc = iterator.next();
            }
        }
        if (jcc == null) {
            throw new IllegalStateException("Can't get system java compiler. Please add jdk tools.jar to your classpath.");
        }

        this.jc = jcc;
        this.fileManager = jc.getStandardFileManager(null, null, null);
        this.options = Arrays.asList("-encoding", encoding, "-g", "-nowarn");

        setDefaultClasspath(fileManager);
    }

    private void setDefaultClasspath(StandardJavaFileManager fileManager) {
        Set<File> files = new HashSet<File>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        while (loader instanceof URLClassLoader && (!"sun.misc.Launcher$ExtClassLoader".equals(loader.getClass().getName()))) {
            URLClassLoader urlClassLoader = (URLClassLoader) loader;
            for (URL url : urlClassLoader.getURLs()) {
                String file = StringEscapeUtils.unescapeUrl(url.getFile());
                files.add(new File(file));
            }
            loader = loader.getParent();
        }

        if (files.size() > 0) {
            try {
                Iterable<? extends File> list = fileManager.getLocation(StandardLocation.CLASS_PATH);
                for (File file : list) {
                    files.add(file);
                }
                fileManager.setLocation(StandardLocation.CLASS_PATH, files);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public Class<?> compile(String qualifiedClassName, String source) {
        try {
            File qualifiedJavaFile = classloader.getGeneratedJavaSourceFile(qualifiedClassName);
            File qualifiedClassFile = classloader.getGeneratedJavaClassFile(qualifiedClassName);

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

    private void generateJavaClass(File qualifiedJavaFile, String source) {
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
            throw new CompileErrorException(sb.toString());
        }
    }
}
