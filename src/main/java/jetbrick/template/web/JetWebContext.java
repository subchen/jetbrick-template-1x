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

import java.util.*;
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

    public static final String SERVLET_CONTEXT_SCOPE = "applicationScope";
    public static final String SESSION_SCOPE = "sessionScope";
    public static final String REQUEST_SCOPE = "requestScope";
    public static final String PARAMETER = "parameter";
    public static final String PARAMETER_VALUES = "parameterValues";

    private final ServletContext servletContext;
    private final HttpSession session;
    private final HttpServletRequest request;

    //@formatter:off
    private enum TYPE {
        REQUEST_SCOPE, SESSION_SCOPE, APPLICATION_SCOPE, 
        PARAMETER, PARAMETER_VALUES, 
        HEADER, HEADER_VALUES, 
        COOKIE, 
        INIT_PARAMETER
    }
    //@formatter:on

    public JetWebContext(HttpServletRequest request, HttpServletResponse response) {
        this(request, response, null);
    }

    public JetWebContext(HttpServletRequest request, HttpServletResponse response, Map<String, Object> context) {
        super(context);

        this.request = request;
        this.session = request.getSession();
        this.servletContext = session.getServletContext();

        put(SERVLET_CONTEXT, servletContext);
        put(SESSION, session);
        put(REQUEST, request);
        put(RESPONSE, response);

        put(SERVLET_CONTEXT_SCOPE, TYPE.APPLICATION_SCOPE);
        put(SESSION_SCOPE, TYPE.SESSION_SCOPE);
        put(REQUEST_SCOPE, TYPE.REQUEST_SCOPE);
        put(PARAMETER, TYPE.PARAMETER);
        put(PARAMETER_VALUES, TYPE.PARAMETER_VALUES);
    }

    @Override
    protected boolean isSimpleModel() {
        return false;
    }

    @Override
    public Object get(String name) {
        if (name == null) return null;

        Object value = super.get(name);
        if (value != null) {
            if (value instanceof TYPE) {
                value = createImplicitWebObject(request, (TYPE) value);
                put(name, value); // resolved
            }
            return value;
        }

        value = request.getAttribute(name);
        if (value != null) {
            return value;
        }
        if (session != null) {
            value = session.getAttribute(name);
            if (value != null) {
                return value;
            }
        }
        return servletContext.getAttribute(name);
    }

    private static Map<String, Object> createImplicitWebObject(HttpServletRequest request, TYPE type) {
        switch (type) {
        case REQUEST_SCOPE: {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> e = request.getAttributeNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                map.put(name, request.getAttribute(name));
            }
            return map;
        }
        case SESSION_SCOPE: {
            HashMap<String, Object> map = new HashMap<String, Object>();
            HttpSession session = request.getSession();
            if (session == null) return null;
            Enumeration<String> e = session.getAttributeNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                map.put(name, session.getAttribute(name));
            }
            return map;
        }
        case APPLICATION_SCOPE: {
            HashMap<String, Object> map = new HashMap<String, Object>();
            ServletContext app = request.getSession().getServletContext();
            Enumeration<String> e = app.getAttributeNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                map.put(name, app.getAttribute(name));
            }
            return map;
        }
        case PARAMETER: {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                map.put(name, request.getParameter(name));
            }
            return map;
        }
        case PARAMETER_VALUES: {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Enumeration<String> e = request.getParameterNames();
            while (e.hasMoreElements()) {
                String name = (String) e.nextElement();
                map.put(name, request.getParameterValues(name));
            }
            return map;
        }
        default:
            return null;
        }
    }
}
