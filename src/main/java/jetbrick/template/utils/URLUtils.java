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
package jetbrick.template.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * @since 1.2.3
 * @author Guoqiang Chen
 */
public class URLUtils {
    public static final String FILE_PROTOCOL = "file";
    public static final String JAR_PROTOCOL = "jar";
    public static final String ZIP_PROTOCOL = "zip";
    public static final String VFS_PROTOCOL = "vfs";
    public static final String FILE_PROTOCOL_PREFIX = "file:";
    public static final String JAR_PROTOCOL_PREFIX = "jar:";
    public static final String ZIP_PROTOCOL_PREFIX = "zip:";
    public static final String VFS_PROTOCOL_PREFIX = "vfs:";
    public static final String JAR_FILE_SEPARATOR = "!/";

    public static URL fromFile(String file) {
        return fromFile(new File(file));
    }

    public static URL fromFile(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static File toFileObject(URL url) {
        if (url == null) return null;
        return new File(toFilePath(url));
    }

    public static String toFilePath(URL url) {
        if (url == null) return null;

        String protocol = url.getProtocol();
        String file = url.getPath();
        try {
            file = URLDecoder.decode(file, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }

        if (FILE_PROTOCOL.equals(protocol)) {
            return file;
        } else if (JAR_PROTOCOL.equals(protocol) || ZIP_PROTOCOL.equals(protocol)) {
            int ipos = file.indexOf(JAR_FILE_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            }
            if (file.startsWith(FILE_PROTOCOL_PREFIX)) {
                file = file.substring(FILE_PROTOCOL_PREFIX.length());
            }
            return file;
        } else if (VFS_PROTOCOL.equals(protocol)) {
            int ipos = file.indexOf(JAR_FILE_SEPARATOR);
            if (ipos > 0) {
                file = file.substring(0, ipos);
            } else if (file.endsWith("/")) {
                file = file.substring(0, file.length() - 1);
            }
            return file;
        }
        return file;
    }
}
