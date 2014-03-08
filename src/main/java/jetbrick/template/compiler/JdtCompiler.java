/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
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
package jetbrick.template.compiler;

import java.io.*;
import java.net.URL;
import java.util.*;
import jetbrick.template.utils.*;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.*;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.*;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @since 1.2.3
 * @author Guoqiang Chen
 */
public class JdtCompiler extends JavaCompiler {
    final Logger log = LoggerFactory.getLogger(JdtCompiler.class);

    @Override
    protected void generateJavaClass(JavaSource source) throws IOException {
        INameEnvironment env = new NameEnvironment(source);
        IErrorHandlingPolicy policy = DefaultErrorHandlingPolicies.proceedWithAllProblems();
        CompilerOptions options = getCompilerOptions();
        CompilerRequestor requestor = new CompilerRequestor();
        IProblemFactory problemFactory = new DefaultProblemFactory(Locale.getDefault());

        Compiler compiler = new Compiler(env, policy, options, requestor, problemFactory);
        compiler.compile(new ICompilationUnit[] { new CompilationUnit(source) });

        if (requestor.hasErrors()) {
            String sourceCode = source.getSourceCode();
            String[] sourceCodeLines = sourceCode.split("(\r\n|\r|\n)", -1);
            StringBuilder sb = new StringBuilder();
            sb.append("Compilation failed.");
            sb.append('\n');
            for (IProblem p : requestor.getErrors()) {
                sb.append(p.getMessage()).append('\n');
                int start = p.getSourceStart();
                int column = start; // default
                for (int i = start; i >= 0; i--) {
                    char c = sourceCode.charAt(i);
                    if (c == '\n' || c == '\r') {
                        column = start - i;
                        break;
                    }
                }
                sb.append(StringUtils.getPrettyError(sourceCodeLines, p.getSourceLineNumber(), column, p.getSourceStart(), p.getSourceEnd(), 3));
            }
            sb.append(requestor.getErrors().length);
            sb.append(" error(s)\n");
            throw new CompileErrorException(sb.toString());
        }

        requestor.save(source.getOutputdir());
    }

    private CompilerOptions getCompilerOptions() {
        Map<String, String> settings = new HashMap<String, String>();
        settings.put(CompilerOptions.OPTION_LineNumberAttribute, CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_SourceFileAttribute, CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_ReportDeprecation, CompilerOptions.IGNORE);
        settings.put(CompilerOptions.OPTION_Encoding, JavaSource.JAVA_FILE_ENCODING);
        settings.put(CompilerOptions.OPTION_LocalVariableAttribute, CompilerOptions.GENERATE);
        settings.put(CompilerOptions.OPTION_Source, CompilerOptions.VERSION_1_6);
        settings.put(CompilerOptions.OPTION_TargetPlatform, CompilerOptions.VERSION_1_6);
        settings.put(CompilerOptions.OPTION_Compliance, CompilerOptions.VERSION_1_6);

        CompilerOptions options = new CompilerOptions(settings);
        options.parseLiteralExpressionsAsConstants = true;
        return options;
    }

    static class CompilationUnit implements ICompilationUnit {
        final JavaSource source;

        public CompilationUnit(JavaSource source) {
            this.source = source;
        }

        @Override
        public char[] getFileName() {
            return source.getJavaFile().getAbsolutePath().toCharArray();
        }

        @Override
        public char[] getContents() {
            return source.getSourceCode().toCharArray();
        }

        @Override
        public char[] getMainTypeName() {
            String qualifiedClassName = source.getQualifiedClassName();
            int dot = qualifiedClassName.lastIndexOf('.');
            if (dot > 0) {
                return qualifiedClassName.substring(dot + 1).toCharArray();
            }
            return qualifiedClassName.toCharArray();
        }

        @Override
        public char[][] getPackageName() {
            StringTokenizer tokenizer = new StringTokenizer(source.getQualifiedClassName(), ".");
            char[][] result = new char[tokenizer.countTokens() - 1][];
            for (int i = 0; i < result.length; i++) {
                String tok = tokenizer.nextToken();
                result[i] = tok.toCharArray();
            }
            return result;
        }

        @Override
        public boolean ignoreOptionalProblems() {
            return false;
        }
    }

    static class NameEnvironment implements INameEnvironment {
        static final Logger log = LoggerFactory.getLogger(NameEnvironment.class);
        final Map<String, Boolean> cache = new HashMap<String, Boolean>();
        final ClassLoader classLoader;
        final JavaSource source;

        public NameEnvironment(JavaSource source) {
            this.source = source;
            this.classLoader = ClassLoaderUtils.getContextClassLoader();
        }

        @Override
        public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < compoundTypeName.length; i++) {
                if (i > 0) {
                    sb.append('.');
                }
                sb.append(compoundTypeName[i]);
            }
            return findType(sb.toString());
        }

        @Override
        public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < packageName.length; i++) {
                sb.append(packageName[i]).append('.');
            }
            sb.append(typeName);
            return findType(sb.toString());
        }

        private NameEnvironmentAnswer findType(String className) {
            if (className.equals(source.getQualifiedClassName())) {
                return new NameEnvironmentAnswer(new CompilationUnit(source), null);
            }

            InputStream is = null;
            try {
                String resourceName = className.replace('.', '/') + ".class";
                is = classLoader.getResourceAsStream(resourceName);
                if (is != null) {
                    byte[] bytes = IoUtils.toByteArray(is);
                    char[] fileName = resourceName.toCharArray();
                    ClassFileReader classFileReader = new ClassFileReader(bytes, fileName, true);
                    return new NameEnvironmentAnswer(classFileReader, null);
                }
            } catch (org.eclipse.jdt.internal.compiler.classfmt.ClassFormatException e) {
                log.error("Compilation error", e);
            } finally {
                IoUtils.closeQuietly(is);
            }
            return null;
        }

        @Override
        public boolean isPackage(char[][] parentPackageName, char[] packageName) {
            StringBuilder sb = new StringBuilder();
            if (parentPackageName != null) {
                for (char[] p : parentPackageName) {
                    sb.append(p).append('.');
                }
            }
            sb.append(packageName);
            String name = sb.toString();

            Boolean found = cache.get(name);
            if (found != null) {
                return found.booleanValue();
            }

            boolean isPackage = !isJavaClass(name);
            cache.put(name, isPackage);
            return isPackage;
        }

        private boolean isJavaClass(String name) {
            if (name.equals(source.getQualifiedClassName())) {
                return true;
            }

            String resourceName = name.replace('.', '/') + ".class";
            URL url = classLoader.getResource(resourceName);
            return url != null;
        }

        @Override
        public void cleanup() {
        }
    }

    static class CompilerRequestor implements ICompilerRequestor {
        static final Logger log = LoggerFactory.getLogger(CompilerRequestor.class);
        ClassFile[] classFiles;
        IProblem[] errors;

        @Override
        public void acceptResult(CompilationResult result) {
            if (result.hasErrors()) {
                errors = result.getErrors();
            } else {
                classFiles = result.getClassFiles();
            }
        }

        public boolean hasErrors() {
            return errors != null;
        }

        public IProblem[] getErrors() {
            return errors;
        }

        public ClassFile[] getClassFiles() {
            return classFiles;
        }

        public void save(File outputdir) throws IOException {
            if (classFiles == null) return;

            for (ClassFile classFile : classFiles) {
                String fileName = new String(classFile.fileName()) + ".class";
                File javaClassFile = new File(outputdir, fileName);
                FileOutputStream fout = new FileOutputStream(javaClassFile);
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(fout);
                    bos.write(classFile.getBytes());
                    bos.close();
                } finally {
                    IoUtils.closeQuietly(fout);
                }
            }
        }
    }

}
