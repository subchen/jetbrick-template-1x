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

import java.util.ArrayList;
import java.util.List;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.ClassLoaderUtils;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.web.WebResourceLoader;

/**
 * 可以从多个 path 下面加载资源.
 *
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public class MultipathResourceLoader implements ResourceLoader {
    private ResourceLoader[] loaders;

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
        List<ResourceLoader> loaders = new ArrayList<ResourceLoader>();

        String[] paths = basepath.split(",");
        for (String path : paths) {
            ResourceLoader loader;

            int pos = path.indexOf(':');
            if (pos >= 0) {
                String klassAlias = path.substring(0, pos).trim();
                path = path.substring(pos + 1).trim();

                if ("file".equals(klassAlias)) {
                    loader = new FileSystemResourceLoader();
                } else if ("classpath".equals(klassAlias)) {
                    loader = new ClasspathResourceLoader();
                } else if ("jar".equals(klassAlias)) {
                    loader = new JarResourceLoader();
                } else if ("webapp".equals(klassAlias)) {
                    loader = new WebResourceLoader();
                } else {
                    // 可能是用户自定义的 ResourceLoader
                    try {
                        Class<?> klass = ClassLoaderUtils.loadClass(klassAlias);
                        loader = (ResourceLoader) klass.newInstance();
                    } catch (Throwable e) {
                        throw ExceptionUtils.uncheck(e);
                    }
                }
            } else {
                path = path.trim();
                loader = new FileSystemResourceLoader();
            }
            loader.initialize(engine, path, encoding);
            loaders.add(loader);
        }

        this.loaders = loaders.toArray(new ResourceLoader[loaders.size()]);
    }

    @Override
    public Resource load(String name) {
        for (ResourceLoader loader : loaders) {
            Resource resource = loader.load(name);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public List<String> loadAll() {
        List<String> resources = new ArrayList<String>();
        for (ResourceLoader loader : loaders) {
            resources.addAll(loader.loadAll());
        }
        return resources;
    }
}
