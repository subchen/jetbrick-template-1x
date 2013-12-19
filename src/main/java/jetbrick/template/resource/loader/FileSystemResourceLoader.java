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
import jetbrick.template.JetEngine;
import jetbrick.template.resource.FileSystemResource;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.PathUtils;

public class FileSystemResourceLoader implements ResourceLoader {
    private String basepath;
    private String encoding;

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
        this.basepath = PathUtils.getStandardizedTemplateRoot(basepath, true);
        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String pathname = PathUtils.combinePathName(basepath, name, false);
        File file = PathUtils.getCanonicalFile(new File(pathname));
        if (!file.exists()) return null;
        return new FileSystemResource(name, file, encoding);
    }
}
