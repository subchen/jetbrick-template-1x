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
import jetbrick.template.utils.IoUtils;

/**
 * @since 1.2.3
 * @author Guoqiang Chen
 */
public class JavaSource {
    public static final String JAVA_FILE_ENCODING = "utf-8";
    private final String qualifiedClassName;
    private final String sourceCode;
    private final File outputdir;
    private final File javaFile;
    private final File classFile;

    public JavaSource(String qualifiedClassName, String sourceCode, File outputdir) {
        this.qualifiedClassName = qualifiedClassName;
        this.sourceCode = sourceCode;
        this.outputdir = outputdir;

        String fileName = qualifiedClassName.replace('.', '/');
        this.javaFile = new File(outputdir, fileName + ".java");
        this.classFile = new File(outputdir, fileName + ".class");
    }

    public void clean() {
        if (javaFile.exists()) {
            javaFile.delete();
        }
        if (classFile.exists()) {
            classFile.delete();
        }
    }

    public void saveJavaFile() throws IOException {
        javaFile.getParentFile().mkdirs();

        OutputStream out = new FileOutputStream(javaFile);
        try {
            out.write(sourceCode.getBytes(JAVA_FILE_ENCODING));
        } finally {
            IoUtils.closeQuietly(out);
        }
    }

    public String getQualifiedClassName() {
        return qualifiedClassName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public File getOutputdir() {
        return outputdir;
    }

    public File getJavaFile() {
        return javaFile;
    }

    public File getClassFile() {
        return classFile;
    }
}
