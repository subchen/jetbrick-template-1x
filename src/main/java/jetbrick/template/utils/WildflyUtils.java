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
package jetbrick.template.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @since 1.2.3
 * @author Guoqiang Chen
 */
public class WildflyUtils {

    @SuppressWarnings("unchecked")
    public static Set<URL> getClasspathURLs(final Object rootModuleClassLoader, boolean allModules) throws Exception {
        Set<URL> urls = new LinkedHashSet<URL>(256);

        // method.1 (get root urls)
        Method method = rootModuleClassLoader.getClass().getDeclaredMethod("findResources", String.class, Boolean.TYPE);
        method.setAccessible(true);
        Enumeration<URL> url = (Enumeration<URL>) method.invoke(rootModuleClassLoader, "", true);
        while (url.hasMoreElements()) {
            urls.add(url.nextElement());
        }
        // method.2 (多了一个 webapp root dir)
        // urls.addAll(getModuleClassLoaderURLs(rootModuleClassLoader));

        if (allModules) {
            // get all modules
            Object moudle = getFieldValue(rootModuleClassLoader, "module");
            Object moduleLoader = getFieldValue(moudle, "moduleLoader");
            Object mainModuleLoader = getFieldValue(moduleLoader, "mainModuleLoader");
            Map<?, ?> moduleMap = (Map<?, ?>) getFieldValue(mainModuleLoader, "moduleMap");

            for (Object futureModule : moduleMap.values()) {
                Object m = getFieldValue(futureModule, "module");
                Object mcl = getFieldValue(m, "moduleClassLoader");
                urls.addAll(getModuleClassLoaderURLs(mcl));
            }
        }
        return urls;
    }

    private static Set<URL> getModuleClassLoaderURLs(final Object moduleClassLoader) throws Exception {
        Set<URL> urls = new LinkedHashSet<URL>();

        Method method = moduleClassLoader.getClass().getDeclaredMethod("getResourceLoaders");
        method.setAccessible(true);
        Object[] resourceLoaders = (Object[]) method.invoke(moduleClassLoader); // ResourceLoader[]
        if (resourceLoaders != null) {
            for (Object resourceLoader : resourceLoaders) {
                if (resourceLoader != null) {
                    String name = resourceLoader.getClass().getName();
                    if ("org.jboss.modules.NativeLibraryResourceLoader".equals(name) || "org.jboss.modules.FileResourceLoader".equals(name)) {
                        File file = (File) getFieldValue(resourceLoader, "root");
                        urls.add(URLUtils.fromFile(file));
                    } else if ("org.jboss.modules.JarFileResourceLoader".equals(name)) {
                        URL url = (URL) getFieldValue(resourceLoader, "rootUrl");
                        urls.add(url);
                    } else if ("org.jboss.as.server.deployment.module.VFSResourceLoader".equals(name)) {
                        URL url = (URL) getFieldValue(resourceLoader, "rootUrl");
                        urls.add(url);
                    } else {
                        throw new IllegalStateException("Unsupported ResourceLoader: " + name);
                    }
                }
            }
        }

        return urls;
    }

    private static Object getFieldValue(Object object, String name) throws Exception {
        Class<?> cls = object.getClass();
        while (cls != Object.class) {
            try {
                Field field = cls.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException(object.getClass().getName() + '#' + name);
    }
}
