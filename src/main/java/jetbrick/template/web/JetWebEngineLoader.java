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

import javax.servlet.*;
import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import jetbrick.template.resource.loader.FileSystemResourceLoader;

/**
 * 自动初始化加载 JetEngine。
 * <pre><xmp>
 * <context-param>
 *   <param-name>jetbrick-template-config-location</param-name>
 *   <param-value>/WEB-INF/jetbrick-template.properties</param-value>
 * </context-param>
 * 
 * <listener>
 *   <listener-class>jetbrick.template.web.JetWebEngineLoader</listener-class>
 * </listener>
 * </xmp></pre>
 */
public class JetWebEngineLoader implements ServletContextListener {
    private static final String CONFIG_LOCATION = "jetbrick-template-config-location";
    private static JetEngine engine;

    public static boolean unavailable() {
        return engine == null;
    }

    public static JetEngine getJetEngine() {
        if (engine == null) {
            throw new IllegalStateException("Please add JetWebEngineLoader as listener into web.xml");
        }
        return engine;
    }

    public static String getTemplateSuffix() {
        if (engine == null) {
            throw new IllegalStateException("Please add JetWebEngineLoader as listener into web.xml");
        }
        return engine.getConfig().getTemplateSuffix();
    }

    // 允许非 ServletContextListener 方式初始化
    public static void setServletContext(ServletContext servletContext) {
        if (engine == null) {
            initJetWebEngine(servletContext);
        }
    }

    private static void initJetWebEngine(ServletContext sc) {
        JetConfig config = new JetConfig();
        config.load(JetConfig.TEMPLATE_PATH, "/"); // 默认 Webapp 根目录

        String location = sc.getInitParameter(CONFIG_LOCATION);
        if (location != null && location.length() > 0) {
            config.load(sc.getResourceAsStream(location));
        } else {
            config.loadClasspath(JetConfig.DEFAULT_CONFIG_FILE);
        }

        if (FileSystemResourceLoader.class.equals(config.getTemplateLoader())) {
            // 转为 Webapp 的相对路径
            String path = config.getTemplatePath();
            config.load(JetConfig.TEMPLATE_PATH, sc.getRealPath(path));
        }

        engine = new JetWebEngine(config);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initJetWebEngine(sce.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        engine = null;
    }

}
