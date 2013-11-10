package jetbrick.template.web;

import java.io.File;
import java.util.Properties;
import javax.servlet.ServletContext;
import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import jetbrick.template.parser.support.ClassUtils;
import jetbrick.template.utils.PropertiesUtils;
import jetbrick.template.web.struts.JetTemplateEngine;

public class JetEngineManager {
    private static ServletContext servletContext;
    private static String configFile;
    private static volatile JetEngine engine;

    public static void setServletContext(ServletContext servletContext) {
        JetEngineManager.servletContext = servletContext;
    }

    public static void setConfigFile(String configFile) {
        JetEngineManager.configFile = configFile;
    }

    public static JetEngine getJetEngine() {
        if (engine != null) return engine;

        synchronized (JetTemplateEngine.class) {
            if (engine != null) return engine; // double check

            if (servletContext == null) return null;

            Properties config = new Properties();
            if (configFile == null || configFile.length() == 0) {
                config = PropertiesUtils.load(ClassUtils.getContextClassLoader().getResourceAsStream(JetConfig.DEFAULT_CONFIG_FILE));
            } else {
                config = PropertiesUtils.load(new File(servletContext.getRealPath(configFile)));
            }
            if (config == null) {
                config = new Properties();
            }

            String value = config.getProperty(JetConfig.TEMPLATE_PATH);
            if (value == null || value.length() == 0) {
                config.put(JetConfig.TEMPLATE_PATH, servletContext.getRealPath("/"));
            }
            engine = new JetWebEngine(config);
        }
        return engine;
    }
}
