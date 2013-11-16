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
            List<Diagnostic<? extends JavaFileObject>> diagnosticsList = diagnostics.getDiagnostics();
            for (Diagnostic<?> d : diagnosticsList) {
                sb.append(d.getMessage(Locale.ENGLISH)).append('\n');
                sb.append(StringUtils.getPrettyError(sourceLines, (int) d.getLineNumber(), (int) d.getColumnNumber(), (int) d.getPosition(), (int) d.getPosition(), 3));
            }
            sb.append(diagnosticsList.size());
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
