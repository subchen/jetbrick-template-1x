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
package jetbrick.template.utils.finder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;
import jetbrick.template.utils.ClassLoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 专门用来查找指定 annotation 的 class 文件.
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public class AnnotationClassLookupUtils {
    private static final Logger log = LoggerFactory.getLogger(AnnotationClassLookupUtils.class);

    public static Set<Class<?>> getClasses(Class<? extends Annotation>[] annotations, boolean skiperrors) {
        return getClasses((String[]) null, true, annotations, skiperrors);
    }

    public static Set<Class<?>> getClasses(List<String> packageNames, boolean recursive, Class<? extends Annotation>[] annotations, boolean skiperrors) {
        String[] pkgs = packageNames.toArray(new String[packageNames.size()]);
        return getClasses(pkgs, recursive, annotations, skiperrors);
    }

    public static Set<Class<?>> getClasses(String[] packageNames, boolean recursive, Class<? extends Annotation>[] annotations, final boolean skiperrors) {
        final AnnotationClassReader reader = new AnnotationClassReader();
        for (Class<? extends Annotation> annotation : annotations) {
            reader.addAnnotation(annotation);
        }

        final Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        final ClassLoader loader = ClassLoaderUtils.getContextClassLoader();

        FileFinder finder = new FileFinder() {
            @Override
            public void visitFileEntry(FileEntry file) {
                try {
                    if (file.isJavaClass()) {
                        if (reader.isAnnotationed(file.getInputStream())) {
                            addClass(file.getQualifiedJavaName());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private void addClass(String qualifiedClassName) {
                try {
                    Class<?> klass = loader.loadClass(qualifiedClassName);
                    classes.add(klass);
                } catch (ClassNotFoundException e) {
                } catch (Throwable e) {
                    if (skiperrors) {
                        log.warn("Class load error.", e);
                    } else {
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        } else if (e instanceof Error) {
                            throw (Error) e;
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        };

        finder.lookupClasspath(packageNames, recursive);

        return classes;
    }
}
