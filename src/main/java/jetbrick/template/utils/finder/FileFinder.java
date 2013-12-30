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

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import jetbrick.template.utils.ClassLoaderUtils;
import jetbrick.template.utils.IoUtils;

/**
 * 查找指定路径下面的所有匹配得文件.
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public abstract class FileFinder {

    protected boolean acceptDirectory(File dir) {
        return true;
    }

    protected abstract void acceptFile(FileEntryItem file);

    protected abstract void acceptJarEntry(JarEntryItem jar);

    /**
     * 搜索文件系统.
     */
    public void lookupFileSystem(File dir, boolean recursive) {
        doLookupInFileSystem(dir, null, null, recursive);
    }

    /**
     * 搜索所有的 Classpath 的文件.
     */
    public void lookupClasspath() {
        lookupClasspath(null, true);
    }

    /**
     * 搜索指定 package 下面的文件.
     */
    public void lookupClasspath(String[] packageNames, boolean recursive) {
        ClassLoader loader = ClassLoaderUtils.getContextClassLoader();
        if (packageNames == null || packageNames.length == 0) {
            Collection<URL> urls = ClassLoaderUtils.getClasspathURLs(loader);
            doGetClasspathResources(urls, null, recursive);
        } else {
            for (String pkg : packageNames) {
                Collection<URL> urls = ClassLoaderUtils.getClasspathURLs(loader, pkg);
                doGetClasspathResources(urls, pkg.replace('.', '/'), recursive);
            }
        }
    }

    /**
     * 搜索 Jar.
     */
    public void lookupJarEntry(File jarFile, String entryName, boolean recursive) {
        JarFile jar = null;
        try {
            jar = new JarFile(jarFile);
            doLookupInJarFile(jar, entryName, recursive);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(jar);
        }
    }

    private void doGetClasspathResources(Collection<URL> urls, String pkgdir, boolean recursive) {
        for (URL url : urls) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                File file = toFile(url);
                if (file.isDirectory()) {
                    doLookupInFileSystem(file, pkgdir, null, recursive);
                } else {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".jar") || name.endsWith(".zip")) {
                        doLookupInJarFile(url, pkgdir, recursive);
                    }
                }
            } else if ("jar".equals(protocol)) {
                doLookupInJarFile(url, pkgdir, recursive);
            } else {
                throw new IllegalStateException("Unsupported url format: " + url.toString());
            }
        }
    }

    private void doLookupInFileSystem(File dir, String pkgdir, String relativeName, boolean recursive) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                if (recursive && acceptDirectory(file)) {
                    String name = (relativeName == null) ? file.getName() : relativeName + '/' + file.getName();
                    doLookupInFileSystem(file, pkgdir, name, true);
                }
            } else {
                String name = (relativeName == null) ? file.getName() : relativeName + '/' + file.getName();
                acceptFile(new FileEntryItem(file, pkgdir, name));
            }
        }
    }

    private void doLookupInJarFile(URL url, String pkgdir, boolean recursive) {
        JarFile jar = null;
        try {
            if ("jar".equals(url.getProtocol())) {
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                jar = conn.getJarFile();
            } else {
                jar = new JarFile(toFile(url));
            }
            doLookupInJarFile(jar, pkgdir, recursive);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(jar);
        }
    }

    private void doLookupInJarFile(JarFile jar, String pkgdir, boolean recursive) {
        if (pkgdir != null) {
            pkgdir = pkgdir + '/';
        }
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体, 可以是目录和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }
            String entryName = entry.getName();
            if (pkgdir == null) {
                acceptJarEntry(new JarEntryItem(jar, entry, entryName));
            } else if (entryName.startsWith(pkgdir)) {
                entryName = entryName.substring(pkgdir.length());
                if (recursive || entryName.indexOf('/') == -1) {
                    acceptJarEntry(new JarEntryItem(jar, entry, entryName));
                }
            }
        }
    }

    private File toFile(URL url) {
        try {
            String decodeUrl = URLDecoder.decode(url.getFile(), "utf-8");
            return new File(decodeUrl);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    static class FileEntryItem {
        private File file;
        private String pkgdir;
        private String relativeName;

        public FileEntryItem(File file, String pkgdir, String relativeName) {
            this.file = file;
            this.pkgdir = pkgdir;
            this.relativeName = relativeName;
        }

        public File getFile() {
            return file;
        }

        public String getFileName() {
            return file.getName();
        }

        public String getRelativeName() {
            return relativeName;
        }

        public String getQualifiedClassName() {
            String name;
            if (pkgdir != null) {
                name = pkgdir + '/' + relativeName;
            } else {
                name = relativeName;
            }
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace('/', '.');
            }
            throw new IllegalStateException("Resource file is not a Java Class.");
        }
    }

    static class JarEntryItem {
        private JarFile jar;
        private JarEntry entry;
        private String relativeName;

        public JarEntryItem(JarFile jar, JarEntry entry, String relativeName) {
            this.jar = jar;
            this.entry = entry;
            this.relativeName = relativeName;
        }

        public JarFile getJarFile() {
            return jar;
        }

        public JarEntry getJarEntry() {
            return entry;
        }

        public String getFileName() {
            int ipos = relativeName.lastIndexOf('/');
            return ipos != -1 ? relativeName.substring(ipos + 1) : relativeName;
        }

        public String getRelativeName() {
            return relativeName;
        }

        public String getQualifiedClassName() {
            String name = entry.getName();
            if (name.endsWith(".class")) {
                return name.substring(0, name.length() - 6).replace('/', '.');
            }
            throw new IllegalStateException("Resource file is not a Java Class.");
        }
    }
}
