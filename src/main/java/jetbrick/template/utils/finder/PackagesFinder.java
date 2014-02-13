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

import java.util.HashSet;
import java.util.Set;

/**
 * 专门用来查找子包.
 *
 * @since 1.2.2
 * @author Guoqiang Chen
 */
public class PackagesFinder {
    public static Set<String> getPackages(String packageName) {
        final Set<String> results = new HashSet<String>();

        FileFinder finder = new FileFinder() {
            @Override
            public boolean visitSystemDirEntry(SystemFileEntry dir) {
                results.add(dir.getQualifiedJavaName());
                return true;
            }

            @Override
            public void visitZipDirEntry(ZipFileEntry dir) {
                results.add(dir.getQualifiedJavaName());
            }
        };

        finder.lookupClasspath(new String[] { packageName }, true);

        return results;
    }

}
