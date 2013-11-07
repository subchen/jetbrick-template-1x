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
        basepath = PathUtils.getStandardizedPath(basepath, true);

        int separator = basepath.indexOf(JAR_FILE_SEPARATOR);
        if (separator > 0) {
            this.jar = new File(basepath.substring(0, separator));
            this.entry = basepath.substring(separator + JAR_FILE_SEPARATOR.length());
        } else {
            this.jar = new File(basepath);
            this.entry = "";
        }
        this.jar = PathUtils.getCanonicalFile(this.jar);
        this.entry = PathUtils.getStandardizedPath(this.entry, false);

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
        private final String encoding;

        public JarResource(String name, File jar, String entry, String encoding) {
            super(name);
            this.jar = jar;
            this.entry = entry;
            this.encoding = encoding;
        }

        @Override
        public String getAbsolutePath() {
            return jar + JAR_FILE_SEPARATOR + entry;
        }

        @Override
        public char[] getSource() {
            return getSource(encoding);
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
