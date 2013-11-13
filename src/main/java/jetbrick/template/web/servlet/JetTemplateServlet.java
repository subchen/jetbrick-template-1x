package jetbrick.template.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import jetbrick.template.*;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineManager;

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

        JetWebEngineManager.setConfigFile(configFile);
        JetWebEngineManager.setServletContext(getServletContext());
        engine = JetWebEngineManager.getJetEngine();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JetContext context = new JetWebContext(request, response);

        String path = request.getPathInfo();
        if (path == null || path.length() == 0) {
            path = request.getServletPath();
        }
        if (path.endsWith("/")) {
            path = path + "/index.jetx";
        }

        if (engine.lookupResource(path)) {
            JetTemplate template = engine.getTemplate(path);
            template.render(context, response.getOutputStream());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Template not found: " + path);
        }
    }
}
