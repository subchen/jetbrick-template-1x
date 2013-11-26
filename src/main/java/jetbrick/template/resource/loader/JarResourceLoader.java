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
package jetbrick.template.resource.loader;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.*;

public class JarResourceLoader implements ResourceLoader {
    private static final String JAR_FILE_SEPARATOR = "!/";
    private File jar;
    private String entry;
    private String encoding;

    @Override
    public void initialize(String basepath, String encoding) {
        basepath = PathUtils.getStandardizedTemplateRoot(basepath, true);

        int separator = basepath.indexOf(JAR_FILE_SEPARATOR);
        if (separator > 0) {
            this.jar = new File(basepath.substring(0, separator));
            this.entry = basepath.substring(separator + JAR_FILE_SEPARATOR.length());
        } else {
            this.jar = new File(basepath);
            this.entry = "";
        }
        this.jar = PathUtils.getCanonicalFile(this.jar);
        this.entry = PathUtils.getStandardizedTemplateRoot(this.entry, false);

        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String fileEntry = PathUtils.combinePathName(entry, name, true);
        return new JarResource(name, jar, fileEntry, encoding);
    }

    static class JarResource extends Resource {
        private final File jar;
        private final String entry;

        public JarResource(String name, File jar, String entry, String encoding) {
            super(name, encoding);
            this.jar = jar;
            this.entry = entry;
        }

        @Override
        public String getAbsolutePath() {
            return jar + JAR_FILE_SEPARATOR + entry;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            JarFile jarFile = new JarFile(jar);
            JarEntry jarEntry = jarFile.getJarEntry(entry);
            if (jarEntry == null) return null;
            return jarFile.getInputStream(jarEntry);
        }

        @Override
        public char[] getSource(String encoding) {
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(jar);
                JarEntry jarEntry = jarFile.getJarEntry(entry);
                if (jarEntry == null) return null;
                InputStream is = jarFile.getInputStream(jarEntry);
                try {
                    return IoUtils.toCharArray(is, encoding);
                } finally {
                    IoUtils.closeQuietly(is);
                }
            } catch (IOException e) {
                throw ExceptionUtils.uncheck(e);
            } finally {
                IoUtils.closeQuietly(jarFile);
            }
        }

        @Override
        public long lastModified() {
            return jar.lastModified();
        }
    }
}
