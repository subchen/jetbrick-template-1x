package jetbrick.template.web;

import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.*;
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
            Properties properties = new Properties();
            properties.setProperty(JetConfig.TEMPLATE_PATH, servletContext.getRealPath(templatePath));
            config.load(properties);
            
            engine = new JetWebEngine(config);
        }
        return engine;
    }

    public static JetContext createJetContext(HttpServletRequest request, HttpServletResponse response) {
        return new JetWebContext(request, response, null);
    }

    public static JetContext createJetContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        return new JetWebContext(request, response, context);
    }

    public static String getTemplateSuffix() {
        return ".jetx";
    }
}
