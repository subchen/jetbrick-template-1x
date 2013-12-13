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

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 在 classpath 下面自动扫描指定的 Class 文件。
 * 
 * @since 1.1.2
 * @author Guoqiang Chen
 */
public class ClassLookupUtils {

    /**
     * Class File 过滤器
     */
    public static interface ClassFileFilter {

        /**
         * 过滤磁盘文件
         */
        public boolean accept(String klassName, File file, ClassLoader loader);

        /**
         * 过滤 Jar 包中的文件
         */
        public boolean accept(String klassName, JarFile jar, JarEntry entry, ClassLoader loader);
    }

    /**
     * 从所有的 classpath 下面搜索指定的 Class
     */
    public static Collection<Class<?>> getClasses(ClassFileFilter filter) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Collection<URL> classpathURLs = getClasspathURLs(loader);
        Set<Class<?>> klasses = new LinkedHashSet<Class<?>>();
        for (URL url : classpathURLs) {
            String file = StringEscapeUtils.unescapeUrl(url.getFile());
            if (file.toLowerCase().endsWith(".jar") || file.toLowerCase().endsWith(".zip")) {
                lookupClassesInJar(null, url, true, loader, filter, klasses);
            } else {
                lookupClassesInFileSystem(null, new File(file), true, loader, filter, klasses);
            }
        }
        return klasses;
    }

    // 根据 classLoader 找到所有父 ClassLoader 拥有的 Classpath URLs
    private static Collection<URL> getClasspathURLs(ClassLoader classLoader) {
        Collection<URL> urls = new ArrayList<URL>(32);
        ClassLoader loader = classLoader;
        while (loader != null) {
            if ("sun.misc.Launcher$ExtClassLoader".equals(loader.getClass().getName())) {
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
     * 从指定 package 中获取所有的 Class
     * 
     * @param packageName   包名
     * @param recursive     是否递归查找
     * @param filter        class 过滤器
     * @return  所有找到的 Class
     */
    public static Set<Class<?>> getClasses(String packageName, boolean recursive, ClassFileFilter filter) {
        if (packageName == null || packageName.length() == 0) {
            throw new IllegalArgumentException("packageName is empty.");
        }

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packageDirName = packageName.replace('.', '/');
        Collection<URL> urls;
        try {
            Enumeration<URL> dirs = loader.getResources(packageDirName);
            urls = Collections.list(dirs);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Set<Class<?>> klasses = new LinkedHashSet<Class<?>>();
        for (URL url : urls) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                String file = StringEscapeUtils.unescapeUrl(url.getFile());
                lookupClassesInFileSystem(packageName, new File(file), recursive, loader, filter, klasses);
            } else if ("jar".equals(protocol)) {
                lookupClassesInJar(packageName, url, recursive, loader, filter, klasses);
            }
        }
        return klasses;
    }

    /**
     * 以文件的形式来获取包下的所有 Class
     * 
     * @param packageName   JAVA包名
     * @param packagePath   包所在的文件目录
     * @param recursive     是否递归查找
     * @param loader        负责该包的 ClassLoader
     * @param filter        class 过滤器
     * @param klasses       返回找到的 class
     */
    private static void lookupClassesInFileSystem(String packageName, File packagePath, final boolean recursive, ClassLoader loader, ClassFileFilter filter, Set<Class<?>> klasses) {
        if (!packagePath.exists() || !packagePath.isDirectory()) {
            return;
        }
        File[] dirfiles = packagePath.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });

        String packageNamePrefix = "";
        if (packageName != null && packageName.length() > 0) {
            packageNamePrefix = packageName + '.';
        }
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                lookupClassesInFileSystem(packageNamePrefix + file.getName(), file, recursive, loader, filter, klasses);
            } else {
                // 去掉后面的 .class 只留下类名
                String klassName = packageNamePrefix + file.getName().substring(0, file.getName().length() - 6);
                try {
                    if (filter == null || filter.accept(klassName, file, loader)) {
                        Class<?> klass = loader.loadClass(klassName);
                        klasses.add(klass);
                    }
                } catch (Throwable e) {
                }
            }
        }
    }

    /**
     * 以在 Jar 包中获取指定包下的所有 Class
     * 
     * @param packageName   JAVA包名
     * @param jarUrl Jar    包文件对应的 URL
     * @param recursive     是否递归查找
     * @param loader        负责该包的 ClassLoader
     * @param filter        class 过滤器
     * @param klasses       返回找到的 class
     */
    private static void lookupClassesInJar(String packageName, URL jarUrl, boolean recursive, ClassLoader loader, ClassFileFilter filter, Set<Class<?>> klasses) {
        String packageDirName = "";
        if (packageName != null && packageName.length() > 0) {
            packageDirName = packageName.replace('.', '/') + '/';
        }

        JarFile jar = null;
        try {
            if ("jar".equals(jarUrl.getProtocol())) {
                jar = ((JarURLConnection) jarUrl.openConnection()).getJarFile();
            } else {
                String file = StringEscapeUtils.unescapeUrl(jarUrl.getFile());
                jar = new JarFile(file);
            }
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                // 获取jar里的一个实体 可以是目录和一些jar包里的其他文件 如META-INF等文件
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();
                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }
                if (name.startsWith(packageDirName) && name.endsWith(".class")) {
                    if (name.lastIndexOf('/') > packageDirName.length()) {
                        // 在子包内
                        if (!recursive) continue;
                    }
                    // 去掉后面的 .class 只留下类名
                    String klassName = name.substring(0, name.length() - 6);
                    klassName = klassName.replace('/', '.');
                    try {
                        if (filter == null || filter.accept(klassName, jar, entry, loader)) {
                            Class<?> klass = loader.loadClass(klassName);
                            klasses.add(klass);
                        }
                    } catch (Throwable e) {
                    }
                }
            }
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            IoUtils.closeQuietly(jar);
        }
    }
}
