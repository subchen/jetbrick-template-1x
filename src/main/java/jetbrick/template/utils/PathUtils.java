/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
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
        if (name == null || name.length() == 0) throw new IllegalArgumentException("Resource path is null or empty.");
        name = name.replace('\\', '/');

        StringBuilder sb = new StringBuilder(name.length() + 2);
        for (String part : name.split("/")) {
            if (part.length() == 0 || ".".equals(part)) continue;
            if ("..".equals(part)) {
                int pos = sb.lastIndexOf("/");
                if (pos < 0) {
                    throw new IllegalStateException("Resource path is not under template root path: " + name);
                }
                sb.delete(pos, sb.length());
            } else {
                sb.append('/').append(part);
            }
        }
        return sb.toString();
    }

    /**
     * 获得一个规范化的模板 root 路径. like: /folder/folder
     * @param path
     * @param fileSystemPath 对于一个非文件系统的路径，那么必须是 “/" 开头的路径
     */
    public static String getStandardizedTemplateRoot(String path, boolean fileSystemPath) {
        if (path == null) throw new IllegalArgumentException("path is null.");
        if (path.length() == 0) return "/";

        path = path.replace('\\', '/');
        if (path.charAt(0) != '/') {
            if (!fileSystemPath || !IS_WINDOW) {
                path = "/" + path; // 加入前导的 "/"
            }
        }
        if (path.length() > 1 && path.charAt(path.length() - 1) == '/') {
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
        if (!baseFile.startsWith("/")) {
            throw new IllegalArgumentException("BaseFile must be start with '/'.");
        }
        relativeName = relativeName.replace('\\', '/');
        if (relativeName.startsWith("/")) {
            return getStandardizedName(relativeName); // 绝对路径直接返回
        }

        // get parent path
        String path = baseFile;
        int pos = path.lastIndexOf('/');
        if (pos < 0) {
            throw new IllegalStateException("Resource path is not under template root path: " + relativeName);
        }
        path = path.substring(0, pos + 1) + relativeName;
        return getStandardizedName(path);
    }
}
