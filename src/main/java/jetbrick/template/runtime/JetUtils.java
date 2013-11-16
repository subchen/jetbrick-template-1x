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
package jetbrick.template.runtime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.*;
import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.resource.Resource;
import jetbrick.template.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JetUtils {
    private static final Logger log = LoggerFactory.getLogger(JetUtils.class);

    public static boolean asBoolean(Object value) {
        if (value == null) return false;

        Class<?> klass = value.getClass();
        if (Boolean.class.equals(klass)) return (Boolean) value;
        if (value instanceof Collection) return ((Collection<?>) value).isEmpty();
        if (value instanceof Map) return ((Map<?, ?>) value).isEmpty();
        if (value instanceof CharSequence) return ((CharSequence) value).length() > 0;
        if (value instanceof Number) return ((Number) value).intValue() != 0;
        if (klass.isArray()) return Array.getLength(value) > 0;
        if (value instanceof Character) return ((Character) value) != '\0';
        if (value instanceof Enumeration) return ((Enumeration<?>) value).hasMoreElements();
        return true;
    }

    public static Iterator<?> asIterator(Object value) {
        if (value == null) {
            return EmptyIterator.INSTANCE;
        }
        if (value instanceof Iterable) {
            return ((Iterable<?>) value).iterator();
        }
        if (value instanceof Map) {
            return ((Map<?, ?>) value).entrySet().iterator();
        }
        if (value instanceof Enumeration) {
            return Collections.list((Enumeration<?>) value).iterator();
        }
        if (value.getClass().isArray()) {
            return new ArrayIterator(value);
        }
        return Collections.singleton(value).iterator();
    }

    public static Map<?, ?> asMap(Object... values) {
        if (values == null || values.length == 0) {
            return Collections.EMPTY_MAP;
        }
        Map<Object, Object> map = new HashMap<Object, Object>(values.length);
        for (int i = 0; i < values.length; i += 2) {
            map.put(values[i], values[i + 1]);
        }
        return map;
    }

    public static byte[] asBytes(String value, String encoding) {
        try {
            return value.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public static boolean asEquals(Object obj1, Object obj2) {
        if (obj1 == obj2) return true;
        if (obj1 == null || obj2 == null) return false;

        if (obj1 instanceof Number && obj2 instanceof Number) {
            return ((Number) obj1).doubleValue() == ((Number) obj2).doubleValue();
        }
        return obj1.equals(obj2);
    }

    public static boolean asNotEquals(Object obj1, Object obj2) {
        return !asEquals(obj1, obj2);
    }

    @SuppressWarnings("unchecked")
    public static int asCompareWith(Object lhs, Object rhs) {
        if (lhs == rhs) return 0;

        // null compare
        if (lhs == null) return -1;
        if (rhs == null) return 1;

        // numeric compare
        if (Character.class.equals(lhs)) {
            lhs = Integer.valueOf(((Character) lhs).charValue());
        }
        if (Character.class.equals(rhs)) {
            rhs = Integer.valueOf(((Character) rhs).charValue());
        }
        if (lhs instanceof Number && rhs instanceof Number) {
            return (int) (((Number) lhs).doubleValue() - ((Number) rhs).doubleValue());
        }

        // object compare
        if (lhs instanceof Comparable) {
            return ((Comparable<Object>) lhs).compareTo(rhs);
        }

        throw new ClassCastException(lhs.getClass().getName() + " cannot be cast to java.util.Comparable");
    }

    public static String asEscapeHtml(String value) {
        return StringEscapeUtils.escapeXml(value);
    }

    // render 子模板，并直接输出
    public static void asInclude(JetPageContext ctx, String relativeName, Map<String, Object> parameters) throws IOException {
        if (relativeName == null || relativeName.length() == 0) {
            throw new IllegalArgumentException("argument relativeName is null or empty.");
        }
        String file = ctx.getAbsolutionName(relativeName);
        JetTemplate template = ctx.getEngine().getTemplate(file);
        JetContext context = new JetContext(ctx.getContext(), parameters);
        JetWriter writer = ctx.getWriter();
        template.render(context, writer);
        writer.println(); // append CRLF for #include directive
    }

    // render 子模板，并返回生成的内容
    public static String asIncludeContent(JetPageContext ctx, String relativeName, Map<String, Object> parameters) {
        if (relativeName == null || relativeName.length() == 0) {
            throw new IllegalArgumentException("argument relativeName is null or empty.");
        }
        String file = ctx.getAbsolutionName(relativeName);
        JetTemplate template = ctx.getEngine().getTemplate(file);
        JetContext context = new JetContext(ctx.getContext(), parameters);

        UnsafeCharArrayWriter os = new UnsafeCharArrayWriter();
        template.render(context, os);
        return os.toString();
    }

    // 读取纯文本内容
    public static String asReadContent(JetPageContext ctx, String relativeName, String encoding) {
        if (relativeName == null || relativeName.length() == 0) {
            throw new IllegalArgumentException("argument relativeName is null or empty.");
        }
        String file = ctx.getAbsolutionName(relativeName);
        Resource resource = ctx.getEngine().getResource(file);
        if (encoding == null) {
            encoding = ctx.getEngine().getConfig().getOutputEncoding();
        }
        return new String(resource.getSource(encoding));
    }

    public static void debug(String format, Object... args) {
        if (log.isDebugEnabled()) {
            format = "template debug: " + format;
            log.debug(format, args);
        }
    }
}
