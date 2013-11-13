package jetbrick.template.utils;

public final class ArrayUtils {
    public static final int[] EMPTY_INTEGER_ARRAY = new int[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    public static int[] range(int start, int stop, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be zero.");
        }
        int length = (stop - start + step) / step;
        if (length <= 0) return ArrayUtils.EMPTY_INTEGER_ARRAY;

        int[] results = new int[length];
        if (step > 0) {
            for (int i = 0, n = start; n <= stop; n += step) {
                results[i++] = n;
            }
        } else {
            for (int i = 0, n = start; n >= stop; n += step) {
                results[i++] = n;
            }
        }
        return results;
    }
}
