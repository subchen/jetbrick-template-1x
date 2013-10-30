package jetbrick.template.parser.support;

import java.util.*;

public class ClassUtils {
    private static final Map<String, String> abbreviation_map;
    private static final Set<String> default_package_set;

    /**
     * Returns current thread's context class loader
     */
    public static ClassLoader getContextClassLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
        }
        return loader;
    }

    /**
     * Returns the class represented by qualifiedClassName using the current
     * thread's context class loader.
     * "int" => "I".
     * "java.lang.String[]" => "[Ljava.lang.String;".
     * "java.util.Map.Entry" => "java.util.Map$Entry".
     */
    public static Class<?> getClass(String qualifiedClassName) {
        return getClass(qualifiedClassName, getContextClassLoader());
    }

    /**
     * Returns the class represented by qualifiedClassName using the classLoader.
     * "int" => "I".
     * "java.lang.String[]" => "[Ljava.lang.String;".
     * "java.util.Map.Entry" => "java.util.Map$Entry".
     */
    public static Class<?> getClass(String qualifiedClassName, ClassLoader classLoader) {
        if (qualifiedClassName == null) throw new NullPointerException("className must not be null.");

        try {
            if (abbreviation_map.containsKey(qualifiedClassName)) {
                String clsName = "[" + abbreviation_map.get(qualifiedClassName);
                return Class.forName(clsName, true, classLoader).getComponentType();
            } else {
                return Class.forName(toJvmClassName(qualifiedClassName), true, classLoader);
            }
        } catch (ClassNotFoundException e) {
            // 尝试当做一个内部类去识别
            int lastDotIndex = qualifiedClassName.lastIndexOf('.');
            if (lastDotIndex > 0) {
                qualifiedClassName = qualifiedClassName.substring(0, lastDotIndex) + '$' + qualifiedClassName.substring(lastDotIndex + 1);
                return getClass(qualifiedClassName, classLoader);
            }
        }
        return null;
    }

    /**
     * 返回 JVM 内部的标准类名.
     * "int" => "I".
     * "java.lang.String[]" => "[Ljava.lang.String;".
     */
    private static String toJvmClassName(String className) {
        if (className == null) throw new NullPointerException("className must not be null.");

        className = className.replaceAll("\\s", ""); // delete whitespaces
        if (className.endsWith("[]")) {
            StringBuilder sb = new StringBuilder();
            while (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
                sb.append("[");
            }
            String abbreviation = (String) abbreviation_map.get(className);
            if (abbreviation != null) {
                sb.append(abbreviation);
            } else {
                sb.append("L").append(className).append(";");
            }
            className = sb.toString();
        }
        return className;
    }

    /**
     * Class.isAssignableFrom() 的增强版本。 支持 null, 自动装箱,
     * 以及数字类型的隐私转换
     */
    public static boolean isAssignable(Class<?> lhs, Class<?> rhs) {
        if (lhs == null) return false;
        if (rhs == null) return (!(lhs.isPrimitive()));

        lhs = PrimitiveUtils.asBoxedClass(lhs, true);
        rhs = PrimitiveUtils.asBoxedClass(rhs, true);
        if (lhs.isAssignableFrom(rhs)) {
            return true;
        }

        lhs = PrimitiveUtils.asUnboxedClass(lhs);
        rhs = PrimitiveUtils.asUnboxedClass(rhs);
        if (lhs == null || rhs == null) {
            return false;
        }
        if (Integer.TYPE.equals(rhs)) {
            return ((Long.TYPE.equals(lhs)) || (Float.TYPE.equals(lhs)) || (Double.TYPE.equals(lhs)));
        }
        if (Long.TYPE.equals(rhs)) {
            return ((Float.TYPE.equals(lhs)) || (Double.TYPE.equals(lhs)));
        }
        if (Float.TYPE.equals(rhs)) {
            return Double.TYPE.equals(lhs);
        }
        if (Double.TYPE.equals(rhs)) {
            return false;
        }
        if (Boolean.TYPE.equals(rhs)) {
            return false;
        }
        if (Byte.TYPE.equals(rhs)) {
            return ((Short.TYPE.equals(lhs)) || (Integer.TYPE.equals(lhs)) || (Long.TYPE.equals(lhs)) || (Float.TYPE.equals(lhs)) || (Double.TYPE.equals(lhs)));
        }
        if (Short.TYPE.equals(rhs)) {
            return ((Integer.TYPE.equals(lhs)) || (Long.TYPE.equals(lhs)) || (Float.TYPE.equals(lhs)) || (Double.TYPE.equals(lhs)));
        }
        if (Character.TYPE.equals(rhs)) {
            return ((Integer.TYPE.equals(lhs)) || (Long.TYPE.equals(lhs)) || (Float.TYPE.equals(lhs)) || (Double.TYPE.equals(lhs)));
        }
        return false;
    }

    // 使用短名字
    public static String getShortClassName(Class<?> klass) {
        if (klass.isPrimitive()) {
            return klass.getName();
        } else {
            String pkg = klass.getPackage().getName();
            if (default_package_set.contains(pkg)) {
                if (klass.isMemberClass()) {
                    // 内部类
                    return klass.getEnclosingClass().getSimpleName() + "." + klass.getSimpleName();
                } else {
                    return klass.getSimpleName();
                }
            }
            return klass.getName();
        }
    }

    static {
        abbreviation_map = new HashMap<String, String>(8);
        abbreviation_map.put("boolean", "Z");
        abbreviation_map.put("byte", "B");
        abbreviation_map.put("short", "S");
        abbreviation_map.put("char", "C");
        abbreviation_map.put("int", "I");
        abbreviation_map.put("long", "J");
        abbreviation_map.put("float", "F");
        abbreviation_map.put("double", "D");

        default_package_set = new HashSet<String>();
        default_package_set.add("java.lang");
        default_package_set.add("java.util");
        default_package_set.add("jetbrick.template.runtime");
        default_package_set.add("jetbrick.template.runtime.methods");
    }
}
