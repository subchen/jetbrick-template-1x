package jetbrick.template.parser.support;

import java.util.HashSet;
import java.util.Set;

public class NumberClassUtils {
    private static final Set<Class<?>> numeric_class_set; // 数字类型
    private static final Set<Class<?>> integer_class_set; // 整数类型

    public static boolean isNumbericClass(Class<?> klass) {
        return numeric_class_set.contains(klass);
    }

    public static boolean isIntegerClass(Class<?> klass) {
        return integer_class_set.contains(klass);
    }

    static {
        numeric_class_set = new HashSet<Class<?>>(14);
        numeric_class_set.add(Byte.TYPE);
        numeric_class_set.add(Short.TYPE);
        numeric_class_set.add(Character.TYPE);
        numeric_class_set.add(Integer.TYPE);
        numeric_class_set.add(Long.TYPE);
        numeric_class_set.add(Float.TYPE);
        numeric_class_set.add(Double.TYPE);
        numeric_class_set.add(Byte.class);
        numeric_class_set.add(Short.class);
        numeric_class_set.add(Character.class);
        numeric_class_set.add(Integer.class);
        numeric_class_set.add(Long.class);
        numeric_class_set.add(Float.class);
        numeric_class_set.add(Double.class);

        integer_class_set = new HashSet<Class<?>>(10);
        integer_class_set.add(Byte.TYPE);
        integer_class_set.add(Short.TYPE);
        integer_class_set.add(Character.TYPE);
        integer_class_set.add(Integer.TYPE);
        integer_class_set.add(Long.TYPE);
        integer_class_set.add(Byte.class);
        integer_class_set.add(Short.class);
        integer_class_set.add(Character.class);
        integer_class_set.add(Integer.class);
        integer_class_set.add(Long.class);
    }
}
