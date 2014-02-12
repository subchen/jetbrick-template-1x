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
public class FileFinder {
    protected final FileVisitor visitor;

    public FileFinder(FileVisitor visitor) {
        this.visitor = visitor;
    }

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
            String name = (relativeName == null) ? file.getName() : relativeName + '/' + file.getName();
            SystemFileEntry entry = new SystemFileEntry(file, pkgdir, name);
            if (file.isDirectory()) {
                if (visitor.visitSystemDirEntry(entry)) {
                    if (recursive) {
                        doLookupInFileSystem(file, pkgdir, name, true);
                    }
                }
            } else {
                visitor.visitSystemFileEntry(entry);
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
        if (pkgdir == null || pkgdir.length() == 0) {
            pkgdir = null;
        } else {
            pkgdir = pkgdir + '/';
        }
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体, 可以是目录和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entry.isDirectory()) {
                entryName = entryName.substring(0, entryName.length() - 1);
            }

            if (pkgdir == null) {
                if (entry.isDirectory()) {
                    visitor.visitJarDirEntry(new JarFileEntry(jar, entry, entryName));
                } else {
                    visitor.visitJarFileEntry(new JarFileEntry(jar, entry, entryName));
                }
            } else if (entryName.startsWith(pkgdir)) {
                entryName = entryName.substring(pkgdir.length());
                if (recursive || entryName.indexOf('/') == -1) {
                    if (entry.isDirectory()) {
                        visitor.visitJarDirEntry(new JarFileEntry(jar, entry, entryName));
                    } else {
                        visitor.visitJarFileEntry(new JarFileEntry(jar, entry, entryName));
                    }
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

    public static interface FileVisitor {
        public boolean visitSystemDirEntry(SystemFileEntry dir);

        public void visitSystemFileEntry(SystemFileEntry file);

        public void visitJarDirEntry(JarFileEntry dir);

        public void visitJarFileEntry(JarFileEntry file);
    }

    public static abstract class SimpleFileVisitor implements FileVisitor {
        @Override
        public boolean visitSystemDirEntry(SystemFileEntry dir) {
            return visitDirEntry(dir);
        }

        @Override
        public void visitSystemFileEntry(SystemFileEntry file) {
            visitFileEntry(file);
        }

        @Override
        public void visitJarDirEntry(JarFileEntry dir) {
            visitDirEntry(dir);
        }

        @Override
        public void visitJarFileEntry(JarFileEntry file) {
            visitFileEntry(file);
        }

        public boolean visitDirEntry(FileEntry dir) {
            return true;
        }

        public void visitFileEntry(FileEntry file) {
        }
    }

    public static interface FileEntry {
        public boolean isDirectory();

        public boolean isJavaClass();

        public String getName();

        public String getRelativePathName();

        public String getQualifiedJavaName();

        public long length();

        public long lastModified();

        public InputStream getInputStream() throws IOException;
    }

    public static class SystemFileEntry implements FileEntry {
        private final File file;
        private final String pkgdir;
        private final String relativeName;

        public SystemFileEntry(File file, String pkgdir, String relativeName) {
            this.file = file;
            this.pkgdir = pkgdir;
            this.relativeName = relativeName;
        }

        public File getFile() {
            return file;
        }

        @Override
        public boolean isDirectory() {
            return file.isDirectory();
        }

        @Override
        public boolean isJavaClass() {
            return !file.isDirectory() && file.getName().endsWith(".class");
        }

        @Override
        public String getName() {
            return file.getName();
        }

        @Override
        public String getRelativePathName() {
            return relativeName;
        }

        @Override
        public String getQualifiedJavaName() {
            String name;
            if (pkgdir != null) {
                name = pkgdir + '/' + relativeName;
            } else {
                name = relativeName;
            }
            if (file.isDirectory()) {
                return name.replace('/', '.');
            } else {
                if (name.endsWith(".class")) {
                    return name.substring(0, name.length() - 6).replace('/', '.');
                }
                throw new IllegalStateException("FileEntry is not a Java Class: " + toString());
            }
        }

        @Override
        public long length() {
            return file.length();
        }

        @Override
        public long lastModified() {
            return file.lastModified();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public String toString() {
            return file.toString();
        }
    }

    public static class JarFileEntry implements FileEntry {
        private final JarFile jar;
        private final JarEntry entry;
        private final String relativeName;

        public JarFileEntry(JarFile jar, JarEntry entry, String relativeName) {
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

        @Override
        public boolean isDirectory() {
            return entry.isDirectory();
        }

        @Override
        public boolean isJavaClass() {
            return entry.getName().endsWith(".class");
        }

        @Override
        public String getName() {
            int ipos = relativeName.lastIndexOf('/');
            return ipos != -1 ? relativeName.substring(ipos + 1) : relativeName;
        }

        @Override
        public String getRelativePathName() {
            return relativeName;
        }

        @Override
        public String getQualifiedJavaName() {
            String name = entry.getName();
            if (entry.isDirectory()) {
                return name.substring(0, name.length() - 1).replace('/', '.');
            } else {
                if (name.endsWith(".class")) {
                    return name.substring(0, name.length() - 6).replace('/', '.');
                }
                throw new IllegalStateException("FileEntry is not a Java Class: " + toString());
            }
        }

        @Override
        public long length() {
            return entry.getSize();
        }

        @Override
        public long lastModified() {
            return entry.getTime();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return jar.getInputStream(entry);
        }

        @Override
        public String toString() {
            return entry.toString();
        }
    }
}
