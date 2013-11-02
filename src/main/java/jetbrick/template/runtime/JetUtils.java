package jetbrick.template.runtime;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
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
        Map<Object, Object> map = new HashMap<Object, Object>(values.length / 2);
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

    public static void asInclude(JetContext context, String relativeName, boolean isPlainText, String encoding) throws IOException {
        if (relativeName == null || relativeName.length() == 0) {
            throw new IllegalArgumentException("argument relativeName is null or empty.");
        }
        String file = PathUtils.relativePath(context.getTemplate().getName(), relativeName);

        if (isPlainText) {
            Resource resource = context.getEngine().getResource(file);
            if (resource == null) {
                throw new IllegalAccessError("FileNotFoundException: " + file);
            }
            if (encoding == null) {
                encoding = context.getEngine().getConfig().getInputEncoding();
            }
            context.getWriter().print(resource.getSource(encoding));
        } else {
            JetTemplate template = context.getEngine().getTemplate(file);
            if (template == null) {
                throw new IllegalAccessError("TemplateNotFound: " + file);
            }
            template.render(context, context.getWriter());
        }
    }

    // redner 子模板， 并返回生成的内容
    public static String include(JetContext context, String relativeName) {
        if (relativeName == null || relativeName.length() == 0) {
            throw new IllegalArgumentException("argument relativeName is null or empty.");
        }
        String file = PathUtils.relativePath(context.getTemplate().getName(), relativeName);
        JetTemplate template = context.getEngine().getTemplate(file);
        if (template == null) {
            throw new IllegalAccessError("TemplateNotFound: " + file);
        }
        StringWriter os = new StringWriter();
        JetWriter out = JetWriter.create(os, context.getEngine().getConfig().getOutputEncoding());
        template.render(context, out);
        return os.toString();
    }

    public static void debug(String format, Object... args) {
        if (log.isDebugEnabled()) {
            format = "template debug: " + format;
            log.debug(format, args);
        }
    }
}
