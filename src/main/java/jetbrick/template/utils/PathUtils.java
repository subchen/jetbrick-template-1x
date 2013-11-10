package jetbrick.template.utils;

import java.io.File;
import java.io.IOException;

public final class PathUtils {
    private static final boolean IS_WINDOW = System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0;

    public static String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static File getCanonicalFile(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static String getCurrentPath() {
        return getCanonicalPath(new File("."));
    }

    // 获得一个规范化的模板文件名称. like: /folder/file.jetx
    public static String getStandardizedName(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("template name is null or empty.");
        name = name.replace('\\', '/');
        if (name.charAt(0) != '/') {
            name = "/" + name; // 加入前导的 "/"
        }
        return name;
    }

    /**
     * 获得一个规范化的模板路径. like: /folder/folder
     * @param path
     * @param fileSystemPath 对于一个非文件系统的路径，那么必须是 “/" 开头的路径
     * @return
     */
    public static String getStandardizedPath(String path, boolean fileSystemPath) {
        if (path == null) throw new IllegalArgumentException("path is null.");
        if (path.length() == 0) return "/";

        path = path.replace('\\', '/');
        if (path.charAt(0) != '/') {
            if (!fileSystemPath || !IS_WINDOW) {
                path = "/" + path; // 加入前导的 "/"
            }
        }
        if (path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1); // 去掉最后的 "/"
        }
        return path;
    }

    // 组合 path 和 name 成新的路径
    public static String combinePathName(String path, String name, boolean removeRootPrefix) {
        String pathname;
        if ("/".equals(path) || path.length() == 0) {
            pathname = name;
        } else {
            pathname = path + name;
        }
        if (removeRootPrefix && pathname.startsWith("/")) {
            pathname = pathname.substring(1); // 删除前导的 "/"
        }
        return pathname;
    }

    // 根据当前模板，找到include子模板的路径。如果 relativeName 以 "/" 开头，那么直接返回 relativeName
    public static String getAbsolutionName(String baseFile, String relativeName) {
        relativeName = relativeName.replace('\\', '/');
        if (relativeName.startsWith("/")) return relativeName; // 绝对路径直接返回

        // get base path
        String path = baseFile;
        int pos = path.lastIndexOf('/');
        path = (pos < 0) ? "" : path.substring(0, pos);

        String[] names = relativeName.split("/");
        for (String name : names) {
            if (".".equals(name)) continue;
            if ("..".equals(name)) {
                pos = path.lastIndexOf('/');
                path = (pos < 0) ? "" : path.substring(0, pos);
            } else {
                path = path + "/" + name;
            }
        }
        return path;
    }
}
