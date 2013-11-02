package jetbrick.template.utils;

import java.text.DecimalFormat;

public final class NumberUtils {
    private static final String DEFAULT_NUMBER_FORMAT = "###,##0.##";

    public static String format(Number value, String format) {
        return value == null ? null : new DecimalFormat(format).format(value);
    }

    public static String format(Number value) {
        return format(value, DEFAULT_NUMBER_FORMAT);
    }

    public static int sum(int[] values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        int sum = 0;
        for (int value : values) {
            sum += value;
        }
        return sum;
    }

    public static int avg(int[] values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        return sum(values) / values.length;
    }

    public static int max(int[] values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public static int min(int[] values) {
        if (values == null || values.length == 0) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (int value : values) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }
}
