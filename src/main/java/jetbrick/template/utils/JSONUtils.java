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
package jetbrick.template.utils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public final class JSONUtils {

    public static String toJSONString(Object object) {
        if (object == null) return "null";
        if (object instanceof Number) return ((Number) object).toString();
        if (object instanceof Boolean) return ((Boolean) object).toString();
        if (object instanceof CharSequence) return stringToJSONString(object.toString());
        if (object instanceof Date) return dateToJSONString((Date) object);
        if (object instanceof Iterable) return iteratorToJSONString(((Iterable<?>) object).iterator());
        if (object instanceof Map) return mapToJSONString((Map<?, ?>) object);
        if (object.getClass().isArray()) return iteratorToJSONString(Arrays.asList((Object[]) object).iterator());
        if (object instanceof Enumeration) return iteratorToJSONString(Collections.list((Enumeration<?>) object).iterator());
        if (object instanceof Iterator) return iteratorToJSONString((Iterator<?>) object);
        if (object instanceof Character) return stringToJSONString(object.toString());
        return beanToJSONString(object);
    }

    private static String stringToJSONString(String str) {
        return '\"' + StringEscapeUtils.escapeJavaScript(str) + '\"';
    }

    private static String dateToJSONString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return stringToJSONString(dateFormat.format(date));
    }

    private static String iteratorToJSONString(Iterator<?> it) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        while (it.hasNext()) {
            if (sb.length() > 1) sb.append(',');
            sb.append(toJSONString(it.next()));
        }
        sb.append(']');
        return sb.toString();
    }

    private static String mapToJSONString(Map<?, ?> map) {
        StringBuffer sb = new StringBuffer();
        sb.append('{');
        Iterator<?> it = map.entrySet().iterator();
        while (it.hasNext()) {
            java.util.Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) it.next();
            if (sb.length() > 1) sb.append(',');
            sb.append(stringToJSONString(entry.getKey().toString()));
            sb.append(':');
            sb.append(toJSONString(entry.getValue()));
        }
        sb.append('}');
        return sb.toString();
    }

    private static String beanToJSONString(Object bean) {
        try {
            Method method = bean.getClass().getMethod("toJSONString", (Class<?>[]) null);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Object result = method.invoke(bean, (Object[]) null);
            return result == null ? "null" : result.toString();
        } catch (NoSuchMethodException e) {
            return stringToJSONString(bean.toString());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
