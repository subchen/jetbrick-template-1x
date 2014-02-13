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
import java.util.List;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.JarResource;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.PathUtils;
import jetbrick.template.utils.finder.TemplateFileFinder;

public class JarResourceLoader implements ResourceLoader {
    private File jarFile;
    private String entryName;
    private String suffix;
    private String encoding;

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
        basepath = PathUtils.getStandardizedTemplateRoot(basepath, true);

        int separator = basepath.indexOf(JarResource.JAR_FILE_SEPARATOR);
        if (separator > 0) {
            this.jarFile = new File(basepath.substring(0, separator));
            this.entryName = basepath.substring(separator + JarResource.JAR_FILE_SEPARATOR.length());
        } else {
            this.jarFile = new File(basepath);
            this.entryName = "";
        }
        this.jarFile = PathUtils.getCanonicalFile(this.jarFile);
        this.entryName = PathUtils.getStandardizedTemplateRoot(this.entryName, false);
        this.suffix = engine.getConfig().getTemplateSuffix();
        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String fileEntry = PathUtils.combinePathName(entryName, name, true);
        return new JarResource(name, jarFile, fileEntry, encoding);
    }

    @Override
    public List<String> loadAll() {
        TemplateFileFinder finder = new TemplateFileFinder(suffix);
        finder.lookupZipFile(jarFile, entryName, true);
        return finder.getResources();
    }
}
