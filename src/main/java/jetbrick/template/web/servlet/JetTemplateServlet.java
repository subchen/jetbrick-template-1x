package jetbrick.template.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import jetbrick.template.*;
import jetbrick.template.parser.support.ClassUtils;
import jetbrick.template.utils.PropertiesUtils;

/**
 * 直接作为 Servlet 使用。需要在 web.xml 中作如下配置。
 * <pre><xmp>
 * <servlet>
 *      <servlet-name>jetbrick-template</servlet-name>
 *      <servlet-class>jetbrick.template.web.servlet.JetTemplateServlet</servlet-class>
 *      <init-param>
 *          <param-name>config</param-name>
 *          <param-value>/WEB-INF/jetbrick-template.properties</param-value>
 *      </init-param>
 *      <load-on-startup>1</load-on-startup>
 *  </servlet>
 *  <servlet-mapping>
 *      <servlet-name>jetbrick-template</servlet-name>
 *      <url-pattern>*.jetx</url-pattern>
 *  </servlet-mapping>
 *  </xmp></pre>
 */
public class JetTemplateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static JetEngine engine;

    @Override
    public void init() throws ServletException {
        String configFile = getInitParameter("config");

        Properties config = new Properties();
        if (configFile == null || configFile.length() == 0) {
            config = PropertiesUtils.load(ClassUtils.getContextClassLoader().getResourceAsStream(JetConfig.DEFAULT_CONFIG_FILE));
        } else {
            config = PropertiesUtils.load(new File(getServletContext().getRealPath(configFile)));
        }
        if (config == null) {
            config = new Properties();
        }

        String value = config.getProperty(JetConfig.TEMPLATE_PATH);
        if (value == null || value.length() == 0) {
            config.put(JetConfig.TEMPLATE_PATH, getServletContext().getRealPath("/"));
        }
        engine = new JetWebEngine(config);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> context = new JetWebContext(getServletContext(), request, response);

        String path = request.getPathInfo();
        if (path == null || path.length() == 0) {
            path = request.getServletPath();
        }
        if (path.endsWith("/")) {
            path = path + "/index.jetx";
        }

        JetTemplate template = engine.getTemplate(path);
        if (template == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Template not found: " + path);
            return;
        }
        template.render(context, response.getOutputStream());
    }
}
