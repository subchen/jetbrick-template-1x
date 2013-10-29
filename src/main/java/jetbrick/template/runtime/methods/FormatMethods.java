package jetbrick.template.runtime.methods;

import java.text.DecimalFormat;
import java.util.Date;
import jetbrick.template.utils.FastDateFormat;

public class FormatMethods {
    private static final String DEFAULT_NUMBER_FORMAT = "###,##0.##";
    private static final FastDateFormat DEFAULT_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static String format(byte value, String format) {
        return format(Byte.valueOf(value), format);
    }

    public static String format(short value, String format) {
        return format(Short.valueOf(value), format);
    }

    public static String format(int value, String format) {
        return format(Integer.valueOf(value), format);
    }

    public static String format(long value, String format) {
        return format(Long.valueOf(value), format);
    }

    public static String format(float value, String format) {
        return format(Float.valueOf(value), format);
    }

    public static String format(double value, String format) {
        return format(Double.valueOf(value), format);
    }

    public static String format(Number value, String format) {
        return value == null ? null : new DecimalFormat(format).format(value);
    }

    public static String format(Number value) {
        return format(value, DEFAULT_NUMBER_FORMAT);
    }

    public static String format(Date value, String format) {
        return FastDateFormat.getInstance(format).format(value);
    }

    public static String format(Date value) {
        return DEFAULT_DATE_FORMAT.format(value);
    }
}
