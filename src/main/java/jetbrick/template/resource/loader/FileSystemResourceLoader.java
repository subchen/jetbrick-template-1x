package jetbrick.template.resource.loader;

import java.io.File;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.IoUtils;
import jetbrick.template.utils.PathUtils;

public class FileSystemResourceLoader implements ResourceLoader {
    private String basepath;
    private String encoding;

    @Override
    public void initialize(String basepath, String encoding) {
        this.basepath = PathUtils.getStandardizedPath(basepath, true);
        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String pathname = PathUtils.combinePathName(basepath, name, false);
        File file = new File(pathname).getAbsoluteFile();
        if (!file.exists()) return null;
        return new FileSystemResource(name, file, encoding);
    }

    static class FileSystemResource extends Resource {
        private final File file;
        private final String encoding;

        public FileSystemResource(String name, File file, String encoding) {
            super(name);
            this.file = file;
            this.encoding = encoding;
        }

        @Override
        public String getAbsolutePath() {
            return file.getAbsolutePath();
        }

        @Override
        public char[] getSource() {
            return IoUtils.toCharArray(file, encoding);
        }

        @Override
        public char[] getSource(String encoding) {
            return IoUtils.toCharArray(file, encoding);
        }

        @Override
        public long lastModified() {
            return file.lastModified();
        }
    }
}
