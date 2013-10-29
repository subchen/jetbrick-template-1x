package jetbrick.template.runtime.methods;

public final class MathMethods {

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
