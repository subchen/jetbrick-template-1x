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
package jetbrick.template.utils.finder;

import java.util.ArrayList;
import java.util.List;

/**
 * 专门用来查找模板 jetx 文件.
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public class TemplateFileFinder extends FileFinder {
    private final List<String> resources = new ArrayList<String>();
    private final String suffix;

    public TemplateFileFinder(String suffix) {
        this.suffix = suffix;
    }

    @Override
    protected void acceptFile(final FileEntryItem file) {
        if (file.getFileName().endsWith(suffix)) {
            resources.add(file.getRelativeName());
        }
    }

    @Override
    protected void acceptJarEntry(final JarEntryItem jar) {
        if (jar.getFileName().endsWith(suffix)) {
            resources.add(jar.getRelativeName());
        }
    }

    /**
     * 返回所有找到的模板文件.
     */
    public List<String> getResources() {
        return resources;
    }
}
