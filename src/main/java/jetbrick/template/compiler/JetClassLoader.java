package jetbrick.template.compiler;

import java.io.File;
import java.net.*;
import jetbrick.template.utils.ExceptionUtils;

public class JetClassLoader {
    private final String classpath;
    private final URL[] urls;
    private final ClassLoader classloader;
    private final boolean reloadable;

    public JetClassLoader(String classpath, boolean reloadable) {
        this.classpath = classpath;
        this.urls = new URL[] { toURL(classpath) };
        this.classloader = new URLClassLoader(urls);
        this.reloadable = reloadable;
    }

    public Class<?> loadClass(String qualifiedClassName) throws ClassNotFoundException {
        if (reloadable) {
            ClassLoader classloader = new URLClassLoader(urls);
            return classloader.loadClass(qualifiedClassName);
        } else {
            return classloader.loadClass(qualifiedClassName);
        }
    }

    public String getClasspath() {
        return classpath;
    }

    private static URL toURL(String url) {
        try {
            return new File(url).toURI().toURL();
        } catch (MalformedURLException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
