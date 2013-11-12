package jetbrick.template.web;

import javax.servlet.ServletContext;
import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import jetbrick.template.web.struts.JetTemplateEngine;

public class JetWebEngineManager {
    private static volatile JetEngine engine;
    private static ServletContext servletContext;
    private static String templatePath = "/";
    private static String configFile;

    public static void setServletContext(ServletContext servletContext) {
        JetWebEngineManager.servletContext = servletContext;
    }

    public static void setTemplatePath(String templatePath) {
        JetWebEngineManager.templatePath = templatePath;
    }

    public static void setConfigFile(String configFile) {
        JetWebEngineManager.configFile = configFile;
    }

    public static JetEngine getJetEngine() {
        if (engine != null) return engine;

        synchronized (JetTemplateEngine.class) {
            if (engine != null) return engine; // double check

            JetConfig config = new JetConfig();
            if (configFile != null && configFile.length() > 0) {
                config.load(servletContext.getResourceAsStream(configFile));
            } else {
                config.loadClasspath(JetConfig.DEFAULT_CONFIG_FILE);
            }
            config.load(JetConfig.TEMPLATE_PATH, servletContext.getRealPath(templatePath));
            engine = new JetWebEngine(config);
        }
        return engine;
    }

    public static String getTemplateSuffix() {
        return ".jetx";
    }
}
