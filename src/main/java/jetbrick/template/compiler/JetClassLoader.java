package jetbrick.template.compiler;

import java.io.File;
import java.net.*;
import jetbrick.template.JetEngine;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetClassLoader {
    private final Logger log = LoggerFactory.getLogger(JetClassLoader.class);
    private final String classpath;
    private final URL[] urls;
    private final ClassLoader classloader;
    private final boolean reloadable;

    public JetClassLoader(String classpath, boolean reloadable) {
        this.classpath = getVersionClasspath(classpath);
        this.urls = new URL[] { toURL(this.classpath) };
        this.classloader = new URLClassLoader(urls);
        this.reloadable = reloadable;

        log.debug("Will compile template into " + this.classpath);
    }

    public Class<?> loadClass(String qualifiedClassName) throws ClassNotFoundException {
        if (reloadable) {
            // 为了可以动态卸载 Class，需要每次重新 new 一个 URLClassLoader
            ClassLoader classloader = new URLClassLoader(urls);
            return classloader.loadClass(qualifiedClassName);
        } else {
            return classloader.loadClass(qualifiedClassName);
        }
    }

    public String getClasspath() {
        return classpath;
    }

    // 根据不同的 Version 生成不同的 classpath
    private static String getVersionClasspath(String classpath) {
        File dir = new File(classpath, "jetx_" + JetEngine.VERSION.replace('.', '_'));
        dir.mkdirs(); // 必须先建立目录，否则 URLClassLoader 会失败
        return PathUtils.getCanonicalPath(dir);
    }

    private static URL toURL(String url) {
        try {
            return new File(url).toURI().toURL();
        } catch (MalformedURLException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
