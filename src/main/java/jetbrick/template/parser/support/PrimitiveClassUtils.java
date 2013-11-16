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
package jetbrick.template.parser.support;

import java.util.*;

public class PrimitiveClassUtils {
    private static final Set<Class<?>> boxed_class_set;
    private static final Map<Class<?>, Class<?>> boxed_class_map;
    private static final Map<Class<?>, Class<?>> unboxed_class_map;
    private static final Map<Class<?>, String> default_value_map;

    public static boolean isBoxedClass(Class<?> klass) {
        return boxed_class_set.contains(klass);
    }

    public static boolean isUnboxedClass(Class<?> klass) {
        return klass != null && klass.isPrimitive();
    }

    public static Class<?> asBoxedClass(Class<?> klass) {
        return boxed_class_map.get(klass);
    }

    public static Class<?> asBoxedClass(Class<?> klass, boolean keepIfNotFound) {
        Class<?> c = boxed_class_map.get(klass);
        if (c == null && keepIfNotFound) {
            c = klass;
        }
        return c;
    }

    public static Class<?> asUnboxedClass(Class<?> klass) {
        return unboxed_class_map.get(klass);
    }

    public static Class<?> asUnboxedClass(Class<?> klass, boolean keepIfNotFound) {
        Class<?> c = unboxed_class_map.get(klass);
        if (c == null && keepIfNotFound) {
            c = klass;
        }
        return c;
    }

    public static String getDefaultValueAsSource(Class<?> klass) {
        if (klass == null) return "null";
        String value = default_value_map.get(klass);
        return value == null ? "null" : value;
    }

    static {
        boxed_class_set = new HashSet<Class<?>>();
        boxed_class_set.add(Boolean.class);
        boxed_class_set.add(Byte.class);
        boxed_class_set.add(Short.class);
        boxed_class_set.add(Character.class);
        boxed_class_set.add(Integer.class);
        boxed_class_set.add(Long.class);
        boxed_class_set.add(Float.class);
        boxed_class_set.add(Double.class);

        unboxed_class_map = new HashMap<Class<?>, Class<?>>(32);
        unboxed_class_map.put(Boolean.class, Boolean.TYPE);
        unboxed_class_map.put(Byte.class, Byte.TYPE);
        unboxed_class_map.put(Short.class, Short.TYPE);
        unboxed_class_map.put(Character.class, Character.TYPE);
        unboxed_class_map.put(Integer.class, Integer.TYPE);
        unboxed_class_map.put(Long.class, Long.TYPE);
        unboxed_class_map.put(Float.class, Float.TYPE);
        unboxed_class_map.put(Double.class, Double.TYPE);
        unboxed_class_map.put(Boolean.TYPE, Boolean.TYPE);
        unboxed_class_map.put(Byte.TYPE, Byte.TYPE);
        unboxed_class_map.put(Short.TYPE, Short.TYPE);
        unboxed_class_map.put(Character.TYPE, Character.TYPE);
        unboxed_class_map.put(Integer.TYPE, Integer.TYPE);
        unboxed_class_map.put(Long.TYPE, Long.TYPE);
        unboxed_class_map.put(Float.TYPE, Float.TYPE);
        unboxed_class_map.put(Double.TYPE, Double.TYPE);

        boxed_class_map = new HashMap<Class<?>, Class<?>>(32);
        boxed_class_map.put(Boolean.TYPE, Boolean.class);
        boxed_class_map.put(Byte.TYPE, Byte.class);
        boxed_class_map.put(Short.TYPE, Short.class);
        boxed_class_map.put(Character.TYPE, Character.class);
        boxed_class_map.put(Integer.TYPE, Integer.class);
        boxed_class_map.put(Long.TYPE, Long.class);
        boxed_class_map.put(Float.TYPE, Float.class);
        boxed_class_map.put(Double.TYPE, Double.class);
        boxed_class_map.put(Boolean.class, Boolean.class);
        boxed_class_map.put(Byte.class, Byte.class);
        boxed_class_map.put(Short.class, Short.class);
        boxed_class_map.put(Character.class, Character.class);
        boxed_class_map.put(Integer.class, Integer.class);
        boxed_class_map.put(Long.class, Long.class);
        boxed_class_map.put(Float.class, Float.class);
        boxed_class_map.put(Double.class, Double.class);

        default_value_map = new HashMap<Class<?>, String>();
        default_value_map.put(Boolean.TYPE, "false");
        default_value_map.put(Byte.TYPE, "0");
        default_value_map.put(Short.TYPE, "0");
        default_value_map.put(Character.TYPE, "'\0'");
        default_value_map.put(Integer.TYPE, "0");
        default_value_map.put(Long.TYPE, "0L");
        default_value_map.put(Float.TYPE, "0.0F");
        default_value_map.put(Double.TYPE, "0.0D");
    }
}
