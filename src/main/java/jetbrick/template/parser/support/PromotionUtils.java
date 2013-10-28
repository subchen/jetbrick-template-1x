package jetbrick.template.parser.support;

import java.util.HashMap;
import java.util.Map;

/**
 *  <p>
 *  Unary Operator. <br/>
 *  {@link http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.1}<br/>
 *  <ul>
 *    <li>Each dimension expression in an array creation expression</li>
 *    <li>The index expression in an array access expression </li>
 *    <li>The operand of a unary plus operator + </li>
 *    <li>The operand of a unary minus operator - </li>
 *    <li>The operand of a bitwise complement operator ~ </li>
 *    <li>Each operand, separately, of a shift operator &gt;&gt;, &gt;&gt;&gt;, or &lt;&lt; </li>
 *  </ul>
 *  </p>
 *
 *  <p>
 *  Binary Operator. <br/>
 *  {@link http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2}<br/>
 *  <ul>
 *    <li>The multiplicative operators *, / and % </li>
 *    <li>The addition and subtraction operators for numeric types + and - </li>
 *    <li>The numerical comparison operators &lt;, &lt;=, &gt;, and &gt;= </li>
 *    <li>The numerical equality operators == and != </li>
 *    <li>The integer bitwise operators &, ^, and | </li>
 *    <li>In certain cases, the conditional operator ? : </li>
 *  </ul>
 *  </p>
 *   
 *  <p>
 *  String "+" Operator. <br/>
 *  {@link http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.18.1}<br/>
 *  </p>
 */
public class PromotionUtils {
    private static final Map<Class<?>, Class<?>> unary_promotion_map;

    // +, -, ~
    public static Class<?> get_unary_basic(Class<?> klass, String op) {
        // http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.1
        switch (op.charAt(0)) {
        case '+':
        case '-':
            return unary_promotion_map.get(klass);
        case '~':
            if (NumberUtils.isIntegerClass(klass)) {
                return unary_promotion_map.get(klass);
            }
        }
        return null;
    }

    // ++, --
    public static Class<?> get_unary_inc_dec(Class<?> klass, String op) {
        // ++, --
        switch (op.charAt(0)) {
        case '+':
        case '-':
            if (NumberUtils.isNumbericClass(klass)) {
                return PrimitiveUtils.asUnboxedClass(klass);
            }
        }
        return null;
    }

    // +, -, *, /, %
    public static Class<?> get_binary_basic(Class<?> lhs, Class<?> rhs, String op) {
        switch (op.charAt(0)) {
        case '+':
            // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.18.1
            if (String.class.equals(lhs) || String.class.equals(rhs)) {
                return String.class;
            }
        case '-':
        case '*':
        case '/':
        case '%':
            // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.18.2
            // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.17
            return to_binary_promotion(lhs, rhs);
        }
        return null;
    }

    // <<, >>, >>>
    public static Class<?> get_binary_shift(Class<?> lhs, Class<?> rhs, String op) {
        // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.19
        if (NumberUtils.isIntegerClass(lhs) && NumberUtils.isIntegerClass(rhs)) {
            return PrimitiveUtils.asUnboxedClass(lhs);
        }
        return null;
    }

    // &, |, ^
    public static Class<?> get_binary_bitwise(Class<?> lhs, Class<?> rhs, String op) {
        switch (op.charAt(0)) {
        case '&':
        case '|':
        case '^':
            // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.22.2
            if (Boolean.TYPE.equals(lhs) && Boolean.TYPE.equals(rhs)) {
                return Boolean.TYPE;
            }
            // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.22.1
            if (NumberUtils.isIntegerClass(lhs) && NumberUtils.isIntegerClass(rhs)) {
                return to_binary_promotion(lhs, rhs);
            }
        }
        return null;
    }

    // <, <=, >, >=
    public static Class<?> get_binary_compare(Class<?> lhs, Class<?> rhs, String op) {
        // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.20.1
        if (NumberUtils.isNumbericClass(lhs) || NumberUtils.isNumbericClass(rhs)) {
            return Boolean.TYPE;
        }
        return null;
    }

    // http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
    private static Class<?> to_binary_promotion(Class<?> lhs, Class<?> rhs) {
        if (!NumberUtils.isNumbericClass(lhs)) {
            return null;
        }
        if (!NumberUtils.isNumbericClass(rhs)) {
            return null;
        }

        lhs = PrimitiveUtils.asUnboxedClass(lhs);
        rhs = PrimitiveUtils.asUnboxedClass(rhs);

        if (Double.TYPE.equals(lhs) || Double.TYPE.equals(rhs)) {
            return Double.TYPE;
        } else if (Float.TYPE.equals(lhs) || Float.TYPE.equals(rhs)) {
            return Float.class;
        } else if (Long.TYPE.equals(lhs) || Long.TYPE.equals(rhs)) {
            return Long.TYPE;
        } else {
            return Integer.TYPE;
        }
    }

    // 得到三元表达式的返回类型，即：简单计算两个class的公共父类或者公共接口。
    // http://docs.oracle.com/javase/specs/jls/se7/html/jls-15.html#jls-15.25
    public static TypedKlass getResultClassForConditionalOperator(TypedKlass k1, TypedKlass k2) {
        if (k1 == k2) return k1;

        Class<?> c1 = (k1 == null) ? null : k1.getKlass();
        Class<?> c2 = (k2 == null) ? null : k2.getKlass();

        if (c1 == null) {
            if (c2.isPrimitive()) {
                return TypedKlass.create(PrimitiveUtils.asBoxedClass(c2));
            }
            return k2;
        }
        if (c2 == null) {
            if (c1.isPrimitive()) {
                return TypedKlass.create(PrimitiveUtils.asBoxedClass(c1));
            }
            return k1;
        }

        if (NumberUtils.isNumbericClass(c1) && NumberUtils.isNumbericClass(c2)) {
            c1 = PrimitiveUtils.asUnboxedClass(c1);
            c2 = PrimitiveUtils.asUnboxedClass(c2);
            return TypedKlass.create(to_binary_promotion(c1, c2));
        }

        if (c1.isAssignableFrom(c2)) return k1;
        if (c2.isAssignableFrom(c1)) return k2;

        // 注意：后面的算法不是很好，应该返回的是一个Set<Class>，而不是一个Class
        if (c1.getSuperclass().isAssignableFrom(c2)) {
            return TypedKlass.create(c1.getSuperclass());
        }
        if (c2.getSuperclass().isAssignableFrom(c1)) {
            return TypedKlass.create(c2.getSuperclass());
        }
        for (Class<?> c : c1.getInterfaces()) {
            if (c.isAssignableFrom(c2)) {
                return TypedKlass.create(c);
            }
        }
        for (Class<?> c : c2.getInterfaces()) {
            if (c.isAssignableFrom(c1)) {
                return TypedKlass.create(c);
            }
        }
        return TypedKlass.Object;
    }

    static {
        // http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.2
        unary_promotion_map = new HashMap<Class<?>, Class<?>>(14);
        unary_promotion_map.put(Byte.TYPE, Integer.TYPE);
        unary_promotion_map.put(Short.TYPE, Integer.TYPE);
        unary_promotion_map.put(Character.TYPE, Integer.TYPE);
        unary_promotion_map.put(Integer.TYPE, Integer.TYPE);
        unary_promotion_map.put(Long.TYPE, Long.TYPE);
        unary_promotion_map.put(Float.TYPE, Float.TYPE);
        unary_promotion_map.put(Double.TYPE, Double.TYPE);
        unary_promotion_map.put(Byte.class, Integer.TYPE);
        unary_promotion_map.put(Short.class, Integer.TYPE);
        unary_promotion_map.put(Character.class, Integer.TYPE);
        unary_promotion_map.put(Integer.class, Integer.TYPE);
        unary_promotion_map.put(Long.class, Long.TYPE);
        unary_promotion_map.put(Float.class, Float.TYPE);
        unary_promotion_map.put(Double.class, Double.TYPE);
    }
}
