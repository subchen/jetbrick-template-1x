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
package jetbrick.template.resource;

import java.io.IOException;
import java.io.InputStream;
import jetbrick.template.utils.ClassLoaderUtils;

/**
 * 没有模板源码，以 .class 形式存在的资源.
 *
 * <p>如果  compile.strategy = none， 那么就会使用这种 Resource。</p>
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public class CompiledClassResource extends Resource {
    private Class<?> loadedClass;

    public CompiledClassResource(String name) {
        super(name, "utf-8");
    }

    @Override
    public String getAbsolutePath() {
        return "(unknown)";
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public char[] getSource() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char[] getSource(String encoding) {
        throw new UnsupportedOperationException();
    }

    // 判断 Class 是否存在
    public boolean exist() {
        try {
            loadedClass = ClassLoaderUtils.loadClass(getQualifiedClassName());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    // 返回已经 load 的 Class
    public Class<?> getCompiledClass() {
        return loadedClass;
    }
}
