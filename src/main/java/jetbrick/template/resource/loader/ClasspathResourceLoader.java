package jetbrick.template.resource.loader;

import java.io.File;
import java.net.URL;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.PathUtils;

public class ClasspathResourceLoader implements ResourceLoader {
    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";
    private static final String FILE_PROTOCOL_PREFIX = "file:";
    private static final String JAR_FILE_SEPARATOR = "!/";

    private String basepath;
    private String encoding;

    @Override
    public void initialize(String basepath, String encoding) {
        this.basepath = PathUtils.getStandardizedPath(basepath, false);
        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String pathname = PathUtils.combinePathName(basepath, name, true);
        URL url = Thread.currentThread().getContextClassLoader().getResource(pathname);
        if (url == null) return null;

        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            File file = new File(url.getFile()).getAbsoluteFile();
            return new FileSystemResourceLoader.FileSystemResource(name, file, encoding);
        } else if (JAR_PROTOCOL.equals(url.getProtocol())) {
            String file = url.getFile();
            if (file.startsWith(FILE_PROTOCOL_PREFIX)) {
                file = file.substring(FILE_PROTOCOL_PREFIX.length());
            }
            int separator = file.indexOf(JAR_FILE_SEPARATOR);
            File jar = new File(file.substring(0, separator)).getAbsoluteFile();
            String entry = file.substring(separator + JAR_FILE_SEPARATOR.length());
            entry = PathUtils.getStandardizedPath(entry, false);
            return new JarResourceLoader.JarResource(name, jar, entry, encoding);
        }
        throw new IllegalStateException("cannot load from url: " + url);
    }
}
