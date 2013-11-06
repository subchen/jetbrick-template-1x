package jetbrick.template.web.servlet;

import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 提供默认的 Servlet 对象.
 */
public class JetWebContext extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    private final ServletContext servletContext;
    private final HttpServletRequest request;

    public JetWebContext(ServletContext servletContext, HttpServletRequest request, HttpServletResponse response) {
        this.servletContext = servletContext;
        this.request = request;

        put("application", servletContext);
        put("session", request.getSession(false));
        put("request", request);
        put("response", response);
        put("parameters", request.getParameterMap());
        put("cookies", request.getCookies());
    }

    @Override
    public Object get(Object key) {
        if (key == null) return null;

        Object value = get(key);
        if (value != null) return value;

        String name = key.toString();
        value = request.getAttribute(name);
        if (value != null) return value;
        value = request.getSession().getAttribute(name);
        if (value != null) return value;
        return servletContext.getAttribute(name);
    }
}
