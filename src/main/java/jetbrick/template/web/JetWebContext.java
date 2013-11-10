package jetbrick.template.web;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private final HttpServletRequest request;

    public JetWebContext(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
        this.servletContext = servletContext;
        this.request = request;

        put(SERVLET_CONTEXT, servletContext);
        put(SESSION, request.getSession(false));
        put(REQUEST, request);
        put(RESPONSE, response);
        put(PARAMETERS, request.getParameterMap());
        put(COOKIES, request.getCookies());
    }

    @Override
    public Object get(String name) {
        if (name == null) return null;

        Object value = super.get(name);
        if (value != null) return value;

        value = request.getAttribute(name);
        if (value != null) return value;
        value = request.getSession().getAttribute(name);
        if (value != null) return value;
        return servletContext.getAttribute(name);
    }
}
