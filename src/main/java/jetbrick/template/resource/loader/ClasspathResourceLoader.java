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

import java.io.File;
import java.net.URL;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.*;
import jetbrick.template.utils.PathUtils;

public class ClasspathResourceLoader implements ResourceLoader {
    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";
    private static final String FILE_PROTOCOL_PREFIX = "file:";
    private static final String JAR_FILE_SEPARATOR = JarResource.JAR_FILE_SEPARATOR;

    private String basepath;
    private String encoding;

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
        this.basepath = PathUtils.getStandardizedTemplateRoot(basepath, false);
        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String pathname = PathUtils.combinePathName(basepath, name, true);
        URL url = Thread.currentThread().getContextClassLoader().getResource(pathname);
        if (url == null) return null;

        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            File file = PathUtils.getCanonicalFile(new File(url.getFile()));
            return new FileSystemResource(name, file, encoding);
        } else if (JAR_PROTOCOL.equals(url.getProtocol())) {
            String file = url.getFile();
            if (file.startsWith(FILE_PROTOCOL_PREFIX)) {
                file = file.substring(FILE_PROTOCOL_PREFIX.length());
            }
            int separator = file.indexOf(JAR_FILE_SEPARATOR);
            File jar = PathUtils.getCanonicalFile(new File(file.substring(0, separator)));
            String entry = file.substring(separator + JAR_FILE_SEPARATOR.length());
            return new JarResource(name, jar, entry, encoding);
        }
        throw new IllegalStateException("cannot load from url: " + url);
    }
}
