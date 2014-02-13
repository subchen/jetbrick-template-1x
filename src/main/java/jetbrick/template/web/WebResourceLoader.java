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
package jetbrick.template.web;

import java.io.*;
import java.net.*;
import java.util.List;
import javax.servlet.ServletContext;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.Resource;
import jetbrick.template.resource.loader.ResourceLoader;
import jetbrick.template.utils.PathUtils;
import jetbrick.template.utils.finder.TemplateFileFinder;

/**
 * 负责加载 webapp 下面的资源文件.
 *
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public class WebResourceLoader implements ResourceLoader {
    private JetWebEngine engine;
    private ServletContext servletContext;
    private String basepath;
    private String suffix;
    private String encoding;

    @Override
    public void initialize(JetEngine engine, String basepath, String encoding) {
        this.engine = ((JetWebEngine) engine);
        this.servletContext = this.engine.getServletContext();
        this.basepath = PathUtils.getStandardizedTemplateRoot(basepath, false);
        this.suffix = engine.getConfig().getTemplateSuffix();
        this.encoding = encoding;
    }

    @Override
    public Resource load(String name) {
        String pathname = PathUtils.combinePathName(basepath, name, false);
        try {
            URL url = servletContext.getResource(pathname);
            if (url == null) {
                return null;
            }
            String realpath = servletContext.getRealPath(pathname);
            File file = (realpath == null) ? null : new File(realpath);
            return new WebResource(url, file, name, encoding);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> loadAll() {
        File dir;
        String realpath = servletContext.getRealPath(basepath);
        if (realpath != null) {
            dir = new File(realpath);
        } else {
            // 该死的 weblogic，以war部署的时候，无法使用 getRealPath
            try {
                URL url = servletContext.getResource(basepath);
                if (url == null) {
                    throw new RuntimeException("template.path is not found in WebResourceLoader: " + basepath);
                }
                String decodeUrl = URLDecoder.decode(url.getFile(), "utf-8");
                dir = new File(decodeUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        TemplateFileFinder finder = new TemplateFileFinder(suffix);
        finder.lookupFileSystem(dir, true);
        return finder.getResources();
    }

    static class WebResource extends Resource {
        private final URL url;
        private final File file;

        public WebResource(URL url, File file, String name, String encoding) {
            super(name, encoding);
            this.url = url;
            this.file = file;
        }

        @Override
        public String getAbsolutePath() {
            return "webapp:" + name;
        }

        @Override
        public long lastModified() {
            return file == null ? 1L : file.lastModified();
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return url.openStream();
        }
    }
}
