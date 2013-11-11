package jetbrick.template.web;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.*;
import jetbrick.template.JetContext;

/**
 * 提供默认的 Servlet 对象.
 */
public class JetWebContext extends JetContext {
    public static final String SERVLET_CONTEXT = "servletContext";
    public static final String SESSION = "session";
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    public static final String PARAMETERS = "parameters";
    public static final String COOKIES = "cookies";

    private final ServletContext servletContext;
    private final HttpSession session;
    private final HttpServletRequest request;

    public JetWebContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        this.request = request;
        this.session = request.getSession();
        this.servletContext = session.getServletContext();

        put(SERVLET_CONTEXT, servletContext);
        put(SESSION, session);
        put(REQUEST, request);
        put(RESPONSE, response);
        put(PARAMETERS, request.getParameterMap());
        put(COOKIES, request.getCookies());

        if (context != null) {
            putAll(context);
        }
    }

    @Override
    public Object get(String name) {
        if (name == null) return null;

        Object value = super.get(name);
        if (value != null) return value;

        value = request.getAttribute(name);
        if (value != null) return value;
        value = session.getAttribute(name);
        if (value != null) return value;
        return servletContext.getAttribute(name);
    }
}
