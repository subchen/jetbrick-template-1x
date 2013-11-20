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
import java.net.*;
import jetbrick.template.JetEngine;
import jetbrick.template.parser.support.ClassUtils;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetTemplateClassLoader {
    private final Logger log = LoggerFactory.getLogger(JetTemplateClassLoader.class);
    private final String classpath;
    private final URL[] urls;
    private final ClassLoader classloader;
    private final boolean reloadable;

    public JetTemplateClassLoader(String classpath, boolean reloadable) {
        this.classpath = getVersionClasspath(classpath);
        this.urls = new URL[] { toURL(this.classpath) };
        this.classloader = createClassLoader();
        this.reloadable = reloadable;

        log.info("Will compile template into " + this.classpath);
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

    public String getClasspath() {
        return classpath;
    }

    private ClassLoader createClassLoader() {
        return new URLClassLoader(urls, ClassUtils.getContextClassLoader());
    }

    // 根据不同的 Version 生成不同的 classpath
    private static String getVersionClasspath(String classpath) {
        File dir = new File(classpath, "jetx_" + JetEngine.VERSION.replace('.', '_'));
        // 必须先建立目录，否则 URLClassLoader 会失败
        if (!dir.mkdirs() && !dir.exists()) {
            throw new IllegalStateException("Can't create a directory in " + dir.getAbsolutePath());
        }
        return PathUtils.getCanonicalPath(dir);
    }

    private static URL toURL(String url) {
        try {
            return new File(url).toURI().toURL();
        } catch (MalformedURLException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
