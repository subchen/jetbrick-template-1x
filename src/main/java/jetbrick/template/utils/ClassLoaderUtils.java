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
import java.io.IOException;
import java.lang.reflect.Method;
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
        Collection<URL> urls = new LinkedHashSet<URL>(32);
        ClassLoader loader = classLoader;
        while (loader != null) {
            String klassName = loader.getClass().getName();
            if (EXT_CLASS_LOADER_NAME.equals(klassName)) {
                break;
            }
            if (loader instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) loader).getURLs()) {
                    urls.add(url);
                }
            } else if (klassName.startsWith("weblogic.utils.classloaders.")) {
                // 该死的 WebLogic，只能特殊处理
                // GenericClassLoader, FilteringClassLoader, ChangeAwareClassLoader
                try {
                    Method method = loader.getClass().getMethod("getClassPath");
                    Object result = method.invoke(loader);
                    if (result != null) {
                        String[] paths = StringUtils.split(result.toString(), File.pathSeparatorChar);
                        for (String path : paths) {
                            urls.add(URLUtils.fromFile(path));
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (klassName.startsWith("org.jboss.modules.ModuleClassLoader")) {
                // 该死的 Wildfly 8，只能特殊处理
                try {
                    Set<URL> urlSet = WildflyUtils.getClasspathURLs(loader, false);
                    urls.addAll(urlSet);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            loader = loader.getParent();
        }

        // moved jsp classpath from ServletContext.attributes to System.properties
        String jsp_classpath = System.getProperty("org.apache.catalina.jsp_classpath");
        String classpath = System.getProperty("java.class.path");

        classpath = StringUtils.trimToEmpty(classpath) + File.pathSeparatorChar + StringUtils.trimToEmpty(jsp_classpath);
        if (classpath.length() > 1) {
            String[] paths = StringUtils.split(classpath, File.pathSeparatorChar);
            for (String path : paths) {
                path = path.trim();
                if (path.length() > 0) {
                    URL url = URLUtils.fromFile(path);
                    urls.add(url);
                }
            }
        }

        // 删除 jdk 自带的 jar
        Iterator<URL> it = urls.iterator();
        while (it.hasNext()) {
            String path = it.next().getPath();
            if (path.contains("/jre/lib/")) {
                it.remove();
            }
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
