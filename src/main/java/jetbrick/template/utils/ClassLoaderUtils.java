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
package jetbrick.template.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Moved from ClassUtils.java.
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public class ClassLoaderUtils {
    public static final String EXT_CLASS_LOADER_NAME = "sun.misc.Launcher$ExtClassLoader";
    public static final String APP_CLASS_LOADER_NAME = "sun.misc.Launcher$AppClassLoader";

    private static final Map<String, String> abbreviationMap;

    /**
     * Returns current thread's context class loader
     */
    public static ClassLoader getContextClassLoader() {
        ClassLoader loader = null;
        try {
            loader = Thread.currentThread().getContextClassLoader();
        } catch (Throwable e) {
        }
        if (loader == null) {
            loader = ClassLoaderUtils.class.getClassLoader();
        }
        return loader;
    }

    /**
     * 根据 classLoader 获取所有的 Classpath URLs.
     */
    public static Collection<URL> getClasspathURLs(final ClassLoader classLoader) {
        Collection<URL> urls = new HashSet<URL>(32);
        ClassLoader loader = classLoader;
        while (loader != null) {
            if (EXT_CLASS_LOADER_NAME.equals(loader.getClass().getName())) {
                break;
            }
            if (loader instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) loader).getURLs()) {
                    urls.add(url);
                }
            }
            loader = loader.getParent();
        }
        return urls;
    }

    /**
     * 根据 classLoader 获取指定 package 对应的 URLs.
     */
    public static Collection<URL> getClasspathURLs(ClassLoader classLoader, String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("PackageName must be not null.");
        }
        Collection<URL> urls = new ArrayList<URL>();
        String dirname = packageName.replace('.', '/');
        try {
            Enumeration<URL> dirs = classLoader.getResources(dirname);
            while (dirs.hasMoreElements()) {
                urls.add(dirs.nextElement());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return urls;
    }

    /**
     * 使用默认的 ClassLoader 去载入类.
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(final String qualifiedClassName) throws ClassNotFoundException {
        return loadClass(qualifiedClassName, null);
    }

    /**
     * 使用指定的 ClassLoader 去载入类.
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(final String qualifiedClassName, final ClassLoader classLoader) throws ClassNotFoundException {
        if (qualifiedClassName == null) {
            throw new NullPointerException("qualifiedClassName must not be null.");
        }

        ClassLoader loader = (classLoader == null) ? getContextClassLoader() : classLoader;

        // 尝试基本类型
        if (abbreviationMap.containsKey(qualifiedClassName)) {
            String klassName = '[' + abbreviationMap.get(qualifiedClassName);
            return Class.forName(klassName, false, loader).getComponentType();
        }

        // 尝试用 Class.forName()
        try {
            String klassName = getCanonicalClassName(qualifiedClassName);
            return Class.forName(klassName, false, loader);
        } catch (ClassNotFoundException e) {
        }

        // 尝试当做一个内部类去识别
        if (qualifiedClassName.indexOf('$') == -1) {
            int ipos = qualifiedClassName.lastIndexOf('.');
            if (ipos > 0) {
                try {
                    String klassName = qualifiedClassName.substring(0, ipos) + '$' + qualifiedClassName.substring(ipos + 1);
                    klassName = getCanonicalClassName(klassName);
                    return Class.forName(klassName, false, loader);
                } catch (ClassNotFoundException e) {
                }
            }
        }

        throw new ClassNotFoundException("Class not found: " + qualifiedClassName);
    }

    /**
     * 将 Java 类名转为 {@code Class.forName()} 可以载入的类名格式.
     * <pre>
     * getCanonicalClassName("int") == "int";
     * getCanonicalClassName("int[]") == "[I";
     * getCanonicalClassName("java.lang.String") == "java.lang.String";
     * getCanonicalClassName("java.lang.String[]") == "[Ljava.lang.String;";
     * </pre>
     */
    public static String getCanonicalClassName(String qualifiedClassName) {
        if (qualifiedClassName == null) {
            throw new NullPointerException("qualifiedClassName must not be null.");
        }

        String name = StringUtils.deleteWhitespace(qualifiedClassName);
        if (name.endsWith("[]")) {
            StringBuilder sb = new StringBuilder();

            while (name.endsWith("[]")) {
                name = name.substring(0, name.length() - 2);
                sb.append('[');
            }

            String abbreviation = abbreviationMap.get(name);
            if (abbreviation != null) {
                sb.append(abbreviation);
            } else {
                sb.append('L').append(name).append(';');
            }

            name = sb.toString();
        }
        return name;
    }

    static {
        abbreviationMap = new HashMap<String, String>();
        abbreviationMap.put("boolean", "Z");
        abbreviationMap.put("byte", "B");
        abbreviationMap.put("short", "S");
        abbreviationMap.put("char", "C");
        abbreviationMap.put("int", "I");
        abbreviationMap.put("long", "J");
        abbreviationMap.put("float", "F");
        abbreviationMap.put("double", "D");
    }
}
