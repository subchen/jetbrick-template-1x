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
package jetbrick.template.compiler;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import jetbrick.template.JetConfig;
import jetbrick.template.JetConfig.CompileStrategy;
import jetbrick.template.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetTemplateClassLoader {
    private final Logger log = LoggerFactory.getLogger(JetTemplateClassLoader.class);
    private final String outputdir;
    private final URL[] urls;
    private final ClassLoader classloader;
    private final boolean reloadable;

    public JetTemplateClassLoader(JetConfig config) {
        this.outputdir = getCanonicalClasspath(config.getCompilePath());
        this.urls = new URL[] { URLUtils.fromFile(this.outputdir) };
        this.classloader = createClassLoader();
        this.reloadable = config.isTemplateReloadable() && config.getCompileStrategy() != CompileStrategy.none;

        log.info("Will compile template into " + this.outputdir);
    }

    public Class<?> loadClass(String qualifiedClassName) throws ClassNotFoundException {
        if (reloadable) {
            // 为了可以动态卸载 Class，需要每次重新 new 一个 URLClassLoader
            ClassLoader classloader = createClassLoader();
            return classloader.loadClass(qualifiedClassName);
        } else {
            return classloader.loadClass(qualifiedClassName);
        }
    }

    // 编译的目标路径
    public String getOutputdir() {
        return outputdir;
    }

    // 返回生成的  .java 文件对象
    public File getGeneratedJavaSourceFile(String qualifiedClassName) {
        String fileName = qualifiedClassName.replace('.', '/');
        return new File(outputdir, fileName + ".java");
    }

    // 返回生成的  .class 文件对象
    public File getGeneratedJavaClassFile(String qualifiedClassName) {
        String fileName = qualifiedClassName.replace('.', '/');
        return new File(outputdir, fileName + ".class");
    }

    private ClassLoader createClassLoader() {
        return new URLClassLoader(urls, ClassLoaderUtils.getContextClassLoader());
    }

    private static String getCanonicalClasspath(String classpath) {
        File dir = new File(classpath);
        // 必须先建立目录，否则 URLClassLoader 会失败
        if (!dir.mkdirs() && !dir.exists()) {
            throw new IllegalStateException("Can't create a directory in " + dir.getAbsolutePath());
        }
        return PathUtils.getCanonicalPath(dir);
    }
}
