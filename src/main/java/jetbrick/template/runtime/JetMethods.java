package jetbrick.template.runtime;

import java.util.*;
import jetbrick.template.utils.*;

public final class JetMethods {

    //---- type cast -------------------------------------------------------
    public static Boolean asBoolean(Object value) {
        if (value == null) return null;
        if (value instanceof Boolean) return (Boolean) value;

        value = value.toString().toLowerCase();
        if ("true".equals(value)) return Boolean.TRUE;
        if ("yes".equals(value)) return Boolean.TRUE;
        if ("on".equals(value)) return Boolean.TRUE;
        if ("t".equals(value)) return Boolean.TRUE;
        if ("y".equals(value)) return Boolean.TRUE;
        if ("1".equals(value)) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public static Character asChar(Object value) {
        if (value == null) return '\0';
        if (value instanceof Character) return (Character) value;
        return value.toString().charAt(0);
    }

    public static Byte asByte(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).byteValue();
        return Byte.valueOf(value.toString());
    }

    public static Short asShort(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).shortValue();
        return Short.valueOf(value.toString());
    }

    public static Integer asInt(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        return Integer.valueOf(value.toString());
    }

    public static Long asLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        return Long.valueOf(value.toString());
    }

    public static Float asFloat(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).floatValue();
        return Float.valueOf(value.toString());
    }

    public static Double asDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return Double.valueOf(value.toString());
    }

    public static <T> List<T> asList(Collection<T> values) {
        if (values == null) return null;
        if (values instanceof List) return (List<T>) values;
        return new ArrayList<T>(values);
    }

    public static List<Object> asList(Object[] values) {
        if (values == null) return Collections.emptyList();
        List<Object> list = new ArrayList<Object>(values.length);
        for (Object value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Boolean> asList(boolean[] values) {
        if (values == null) return Collections.emptyList();
        List<Boolean> list = new ArrayList<Boolean>(values.length);
        for (boolean value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Character> asList(char[] values) {
        if (values == null) return Collections.emptyList();
        List<Character> list = new ArrayList<Character>(values.length);
        for (char value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Byte> asList(byte[] values) {
        if (values == null) return Collections.emptyList();
        List<Byte> list = new ArrayList<Byte>(values.length);
        for (byte value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Short> asList(short[] values) {
        if (values == null) return Collections.emptyList();
        List<Short> list = new ArrayList<Short>(values.length);
        for (short value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Integer> asList(int[] values) {
        if (values == null) return Collections.emptyList();
        List<Integer> list = new ArrayList<Integer>(values.length);
        for (int value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Long> asList(long[] values) {
        if (values == null) return Collections.emptyList();
        List<Long> list = new ArrayList<Long>(values.length);
        for (long value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Float> asList(float[] values) {
        if (values == null) return Collections.emptyList();
        List<Float> list = new ArrayList<Float>(values.length);
        for (float value : values) {
            list.add(value);
        }
        return list;
    }

    public static List<Double> asList(double[] values) {
        if (values == null) return Collections.emptyList();
        List<Double> list = new ArrayList<Double>(values.length);
        for (double value : values) {
            list.add(value);
        }
        return list;
    }

    public static Date asDate(String value) {
        return DateUtils.asDate(value);
    }

    public static Date asDate(String value, String format) {
        return DateUtils.asDate(value, format);
    }

    /**
     * 支持输出 "null"
     */
    public String asString(Object value) {
        if (value == null) return "null";
        return value.toString();
    }

    //---- json -------------------------------------------------------
    public static String asJSON(Object object) {
        return JSONUtils.toJSONString(object);
    }

    //---- format -------------------------------------------------------
    public static String format(byte value, String format) {
        return NumberUtils.format(Byte.valueOf(value), format);
    }

    public static String format(short value, String format) {
        return NumberUtils.format(Short.valueOf(value), format);
    }

    public static String format(int value, String format) {
        return NumberUtils.format(Integer.valueOf(value), format);
    }

    public static String format(long value, String format) {
        return NumberUtils.format(Long.valueOf(value), format);
    }

    public static String format(float value, String format) {
        return NumberUtils.format(Float.valueOf(value), format);
    }

    public static String format(double value, String format) {
        return NumberUtils.format(Double.valueOf(value), format);
    }

    public static String format(Number value, String format) {
        return NumberUtils.format(value, format);
    }

    public static String format(Number value) {
        return NumberUtils.format(value);
    }

    public static String format(Date value, String format) {
        return DateUtils.format(value, format);
    }

    public static String format(Date value) {
        return DateUtils.format(value);
    }

    //---- String -------------------------------------------------------
    public static String toUnderlineName(String s) {
        return WordUtils.toUnderlineName(s);
    }

    public static String toCamelCase(String s) {
        return WordUtils.toCamelCase(s);
    }

    public static String toCapitalizeCamelCase(String s) {
        return WordUtils.toCapitalizeCamelCase(s);
    }

    public static String repeat(String value, int count) {
        return StringUtils.repeat(value, count);
    }

    //---- String escape -------------------------------------------------------

    public static String escapeJava(String value) {
        return StringEscapeUtils.escapeJava(value);
    }

    public static String unescapeJava(String value) {
        return StringEscapeUtils.unescapeJava(value);
    }

    public static String escapeJavaScript(String value) {
        return StringEscapeUtils.escapeJavaScript(value);
    }

    public static String unescapeJavaScript(String value) {
        return StringEscapeUtils.unescapeJavaScript(value);
    }

    public static String escapeXml(String value) {
        return StringEscapeUtils.escapeXml(value);
    }

    public static String unescapeXml(String value) {
        return StringEscapeUtils.unescapeXml(value);
    }

    public static String escapeUrl(String value) {
        return StringEscapeUtils.escapeUrl(value);
    }

    public static String escapeUrl(String value, String encoding) {
        return StringEscapeUtils.escapeUrl(value, encoding);
    }

    public static String unescapeUrl(String value) {
        return StringEscapeUtils.unescapeUrl(value);
    }

    public static String unescapeUrl(String value, String encoding) {
        return StringEscapeUtils.unescapeUrl(value, encoding);
    }

    //---- math -------------------------------------------------------
    public static int sum(int[] values) {
        return NumberUtils.sum(values);
    }

    public static int avg(int[] values) {
        return NumberUtils.avg(values);
    }

    public static int max(int[] values) {
        return NumberUtils.max(values);
    }

    public static int min(int[] values) {
        return NumberUtils.min(values);
    }
}
