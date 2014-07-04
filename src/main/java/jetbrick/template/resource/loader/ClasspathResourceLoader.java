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
package jetbrick.template.resource.loader;

import java.io.File;
import java.net.URL;
import java.util.List;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.*;
import jetbrick.template.utils.ClassLoaderUtils;
import jetbrick.template.utils.PathUtils;
import jetbrick.template.utils.finder.TemplateFileFinder;

public class ClasspathResourceLoader implements ResourceLoader {
    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";
    private static final String FILE_PROTOCOL_PREFIX = "file:";
    private static final String JAR_FILE_SEPARATOR = JarResource.JAR_FILE_SEPARATOR;

    private String basepath;
    private String encoding;
    private String suffix;

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
        this.basepath = PathUtils.getStandardizedTemplateRoot(basepath, false);
        this.encoding = encoding;
        this.suffix = engine.getConfig().getTemplateSuffix();
    }

    @Override
    public Resource load(String name) {
        String pathname = PathUtils.combinePathName(basepath, name, true);
        URL url = ClassLoaderUtils.getContextClassLoader().getResource(pathname);
        if (url == null) return null;

        String fileUrl = url.getPath();
        try {
            fileUrl = URLDecoder.decode(fileUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        
        if (FILE_PROTOCOL.equals(url.getProtocol())) {
            File file = PathUtils.getCanonicalFile(new File(fileUrl));
            return new FileSystemResource(name, file, encoding);
        } else if (JAR_PROTOCOL.equals(url.getProtocol())) {
            if (fileUrl.startsWith(FILE_PROTOCOL_PREFIX)) {
                fileUrl = fileUrl.substring(FILE_PROTOCOL_PREFIX.length());
            }
            int separator = fileUrl.indexOf(JAR_FILE_SEPARATOR);
            File jar = PathUtils.getCanonicalFile(new File(fileUrl.substring(0, separator)));
            String entry = fileUrl.substring(separator + JAR_FILE_SEPARATOR.length());
            return new JarResource(name, jar, entry, encoding);
        }

        throw new IllegalStateException("cannot load from url: " + url);
    }

    @Override
    public List<String> loadAll() {
        String[] packageNames = null;
        if (basepath.length() > 1) {
            packageNames = new String[] { basepath.substring(1).replace('/', '.') };
        }

        TemplateFileFinder finder = new TemplateFileFinder(suffix);
        finder.lookupClasspath(packageNames, true);
        return finder.getResources();
    }
}
