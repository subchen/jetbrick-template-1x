package jetbrick.template.runtime.methods;

import java.text.*;
import java.util.*;

public final class TypeCastMethods {
    //@formatter:off
    private static final String[] DATE_PATTERNS = new String[] { 
        "yyyy-MM-dd HH:mm:ss.SSS", 
        "yyyy-MM-dd HH:mm:ss", 
        "yyyy-MM-dd", 
        "HH:mm:ss"
    };
    //@formatter:on

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
        value = (value != null) ? value.trim() : null;
        ParsePosition pp = null;
        Date d = null;
        for (int i = 0; d == null && i < DATE_PATTERNS.length; i++) {
            DateFormat df = new SimpleDateFormat(DATE_PATTERNS[i]);
            df.setLenient(false);
            try {
                pp = new ParsePosition(0);
                d = df.parse(value, pp);
                if (pp.getIndex() != value.length()) {
                    d = null;
                }
            } catch (Exception e) {
                // try next pattern
            }
        }
        return d;
    }

    public static Date asDate(String value, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        try {
            ParsePosition pp = new ParsePosition(0);
            Date d = sdf.parse(value, pp);
            if (pp.getIndex() != value.length()) {
                d = null;
            }
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public String asString(Object value) {
        if (value == null) return null;
        return value.toString();
    }
}
