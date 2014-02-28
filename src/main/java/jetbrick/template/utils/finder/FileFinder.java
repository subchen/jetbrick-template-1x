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
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import jetbrick.template.utils.*;

/**
 * 查找指定路径下面的所有匹配得文件.
 *
 * 子类需要实现自己的 visitXXX() 方法来搜集相关的内容.
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public abstract class FileFinder {

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
        lookupClasspath((String[]) null, true);
    }

    /**
     * 搜索指定 package 下面的文件.
     */
    public void lookupClasspath(List<String> packageNames, boolean recursive) {
        String[] pkgs = null;
        if (packageNames != null) {
            pkgs = packageNames.toArray(new String[packageNames.size()]);
        }
        lookupClasspath(pkgs, recursive);
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
     * 搜索 jar/zip.
     */
    public void lookupZipFile(File zipFile, String entryName, boolean recursive) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile);
            doLookupInZipFile(zip, entryName, recursive);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(zip);
        }
    }

    private void doGetClasspathResources(Collection<URL> urls, String pkgdir, boolean recursive) {
        for (URL url : urls) {
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                File file = URLUtils.toFileObject(url);
                if (file.isDirectory()) {
                    doLookupInFileSystem(file, pkgdir, null, recursive);
                } else {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".jar") || name.endsWith(".zip")) {
                        doLookupInZipFile(url, pkgdir, recursive);
                    }
                }
            } else if ("jar".equals(protocol) || "zip".equals(protocol)) {
                doLookupInZipFile(url, pkgdir, recursive);
            } else if ("vfs".equals(protocol)) {
                doLookupInVfsFile(url, pkgdir, recursive);
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
                if (visitSystemDirEntry(entry)) {
                    if (recursive) {
                        doLookupInFileSystem(file, pkgdir, name, true);
                    }
                }
            } else {
                visitSystemFileEntry(entry);
            }
        }
    }

    private void doLookupInZipFile(URL url, String pkgdir, boolean recursive) {
        ZipFile zip = null;
        try {
            if ("jar".equals(url.getProtocol())) {
                zip = ((JarURLConnection) url.openConnection()).getJarFile();
            } else {
                File file = URLUtils.toFileObject(url);
                if (!file.exists()) {
                    return;
                }
                zip = new ZipFile(file);
            }
            doLookupInZipFile(zip, pkgdir, recursive);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtils.closeQuietly(zip);
        }
    }

    private void doLookupInZipFile(ZipFile zip, String pkgdir, boolean recursive) {
        if (pkgdir == null || pkgdir.length() == 0) {
            pkgdir = null;
        } else {
            pkgdir = pkgdir + '/';
        }
        Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体, 可以是目录和一些jar包里的其他文件 如META-INF等文件
            ZipEntry entry = entries.nextElement();
            String entryName = entry.getName();
            if (entry.isDirectory()) {
                entryName = entryName.substring(0, entryName.length() - 1);
            }

            if (pkgdir == null) {
                if (entry.isDirectory()) {
                    visitZipDirEntry(new ZipFileEntry(zip, entry, entryName));
                } else {
                    visitZipFileEntry(new ZipFileEntry(zip, entry, entryName));
                }
            } else if (entryName.startsWith(pkgdir)) {
                entryName = entryName.substring(pkgdir.length());
                if (recursive || entryName.indexOf('/') == -1) {
                    if (entry.isDirectory()) {
                        visitZipDirEntry(new ZipFileEntry(zip, entry, entryName));
                    } else {
                        visitZipFileEntry(new ZipFileEntry(zip, entry, entryName));
                    }
                }
            }
        }
    }

    private void doLookupInVfsFile(URL url, String pkgdir, boolean recursive) {
        try {
            URLConnection conn = url.openConnection();
            if (conn.getClass().getName().equals("org.jboss.vfs.protocol.VirtualFileURLConnection")) {
                String vfs = conn.getContent().toString(); // VirtualFile
                File file = new File(vfs.substring(1, vfs.length() - 1));
                if (!file.exists()) {
                    return;
                }
                if (file.isDirectory()) {
                    doLookupInFileSystem(file, pkgdir, null, recursive);
                } else {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".jar") || name.endsWith(".zip")) {
                        ZipFile zip = new ZipFile(file);
                        try {
                            doLookupInZipFile(zip, pkgdir, recursive);
                        } finally {
                            IoUtils.closeQuietly(zip);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("Unsupported URL: " + url);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //----------------------------------------------------------------
    // following visitXXX methods should be overrided by subclass.
    //
    protected boolean visitSystemDirEntry(SystemFileEntry dir) {
        return visitDirEntry(dir);
    }

    protected void visitSystemFileEntry(SystemFileEntry file) {
        visitFileEntry(file);
    }

    protected void visitZipDirEntry(ZipFileEntry dir) {
        visitDirEntry(dir);
    }

    protected void visitZipFileEntry(ZipFileEntry file) {
        visitFileEntry(file);
    }

    protected boolean visitDirEntry(FileEntry dir) {
        return true;
    }

    protected void visitFileEntry(FileEntry file) {
    }

    //----------------------------------------------------------------
    // innerclass.
    //
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

    public static class ZipFileEntry implements FileEntry {
        private final ZipFile zip;
        private final ZipEntry entry;
        private final String relativeName;

        public ZipFileEntry(ZipFile zip, ZipEntry entry, String relativeName) {
            this.zip = zip;
            this.entry = entry;
            this.relativeName = relativeName;
        }

        public ZipFile getZipFile() {
            return zip;
        }

        public ZipEntry getZipEntry() {
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
            return zip.getInputStream(entry);
        }

        @Override
        public String toString() {
            return entry.toString();
        }
    }
}
