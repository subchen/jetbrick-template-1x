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

import java.io.File;
import java.io.IOException;
import jetbrick.template.JetConfig;

public abstract class JavaCompiler {
    protected JetTemplateClassLoader classloader;
    protected boolean debugEnabled;

    public static JavaCompiler create(JetTemplateClassLoader classloader, JetConfig config) {
        try {
            JavaCompiler jc = (JavaCompiler) config.getCompileTool().newInstance();
            jc.classloader = classloader;
            jc.debugEnabled = config.isCompileDebug();
            jc.initialize();
            return jc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void initialize() {
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public File getOutputdir() {
        return classloader.getOutputdir();
    }

    public Class<?> compile(JavaSource source) {
        try {
            source.clean();
            source.saveJavaFile();

            generateJavaClass(source);

            return classloader.loadClass(source.getQualifiedClassName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void generateJavaClass(JavaSource source) throws IOException;
}
