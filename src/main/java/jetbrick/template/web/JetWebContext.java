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

    public static final String CONTEXT_PATH = "ctxpath";
    public static final String WEBROOT_PATH = "webroot";

    private final ServletContext servletContext;
    private final HttpSession session;
    private final HttpServletRequest request;

    //@formatter:off
    private enum TYPE {
        REQUEST_SCOPE, SESSION_SCOPE, APPLICATION_SCOPE,
        PARAMETER, PARAMETER_VALUES,
        HEADER, HEADER_VALUES,
        COOKIE,
        INIT_PARAMETER,
        WEBROOT_PATH,
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

        put(CONTEXT_PATH, servletContext.getContextPath());
        put(WEBROOT_PATH, TYPE.WEBROOT_PATH);
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
                value = createImplicitWebObject((TYPE) value);
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

    private Object createImplicitWebObject(TYPE type) {
        switch (type) {
        case REQUEST_SCOPE: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return request.getAttributeNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        return request.getAttribute((String) key);
                    }
                    return null;
                }
            };
        }
        case SESSION_SCOPE: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return session.getAttributeNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        return session.getAttribute((String) key);
                    }
                    return null;
                }
            };
        }
        case APPLICATION_SCOPE: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return servletContext.getAttributeNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        return servletContext.getAttribute((String) key);
                    }
                    return null;
                }
            };
        }
        case PARAMETER: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return request.getParameterNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        return request.getParameter((String) key);
                    }
                    return null;
                }
            };
        }
        case PARAMETER_VALUES: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return request.getParameterNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        return request.getParameterValues((String) key);
                    }
                    return null;
                }
            };
        }
        case HEADER: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return request.getHeaderNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        return request.getHeader((String) key);
                    }
                    return null;
                }
            };
        }
        case HEADER_VALUES: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return request.getHeaderNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        List<String> list = new ArrayList<String>();
                        Enumeration<String> e = request.getHeaders((String) key);
                        if (e != null) {
                            while (e.hasMoreElements()) {
                                list.add(e.nextElement());
                            }
                        }
                        return list.toArray(new String[list.size()]);
                    }
                    return null;
                }
            };
        }
        case COOKIE: {
            Cookie[] cookies = request.getCookies();
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; (cookies != null) && (i < cookies.length); i++) {
                Cookie cookie = cookies[i];
                if (cookie != null) {
                    String name = cookie.getName();
                    if (!map.containsKey(name)) {
                        map.put(name, cookie);
                    }
                }
            }
            return map;
        }
        case INIT_PARAMETER: {
            return new EnumeratedMap<String, Object>() {
                @Override
                public Enumeration<String> enumerateKeys() {
                    return servletContext.getInitParameterNames();
                }

                @Override
                public Object getValue(Object key) {
                    if (key instanceof String) {
                        return servletContext.getInitParameter((String) key);
                    }
                    return null;
                }
            };
        }
        case WEBROOT_PATH: {
            StringBuilder sb = new StringBuilder();
            sb.append(request.getScheme());
            sb.append("://");
            sb.append(request.getServerName());
            sb.append(request.getServerPort() != 80 ? ':' + request.getServerPort() : "");
            sb.append(request.getContextPath());
            return sb.toString();
        }
        default:
            return null;
        }
    }

    private static abstract class EnumeratedMap<K, V> implements Map<K, V> {
        private Map<K, V> map;

        @Override
        public boolean containsKey(Object key) {
            return getValue(key) != null;
        }

        @Override
        public boolean containsValue(Object value) {
            return getAsMap().containsValue(value);
        }

        @Override
        public V get(Object key) {
            return getValue(key);
        }

        @Override
        public boolean isEmpty() {
            return getAsMap().isEmpty();
        }

        @Override
        public int size() {
            return getAsMap().size();
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            return getAsMap().entrySet();
        }

        @Override
        public Set<K> keySet() {
            return getAsMap().keySet();
        }

        @Override
        public Collection<V> values() {
            return getAsMap().values();
        }

        @Override
        public V put(K key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> map) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public abstract Enumeration<K> enumerateKeys();

        public abstract V getValue(Object key);

        public Map<K, V> getAsMap() {
            if (map == null) {
                synchronized (this) {
                    if (map == null) {
                        Map<K, V> result = new HashMap<K, V>();
                        for (Enumeration<K> e = enumerateKeys(); e.hasMoreElements();) {
                            K key = e.nextElement();
                            V value = getValue(key);
                            result.put(key, value);
                        }
                        map = result;
                    }
                }
            }
            return map;
        }
    }
}
