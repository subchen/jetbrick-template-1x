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

import java.util.Collections;
import java.util.List;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.CompiledClassResource;
import jetbrick.template.resource.Resource;

/**
 * 负责 load 那种没有模板源码，以 .class 形式存在的资源.
 *
 * <p>如果  compile.strategy = none， 那么就会使用这种 ClassLoader。</p>
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public class CompiledClassResourceLoader implements ResourceLoader {

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
    }

    @Override
    public Resource load(String name) {
        CompiledClassResource resource = new CompiledClassResource(name);
        return resource.exist() ? resource : null;
    }

    @Override
    public List<String> loadAll() {
        return Collections.emptyList();
    }
}
