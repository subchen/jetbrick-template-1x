package jetbrick.template.runtime;

import java.util.*;
import jetbrick.template.utils.ArrayUtils;

public final class JetFunctions {
    private static final Random RANDOM = new Random();

    public static Date now() {
        return new Date();
    }

    public static int random() {
        return RANDOM.nextInt();
    }

    public static UUID uuid() {
        return UUID.randomUUID();
    }

    public static int[] step(int start, int stop) {
        return ArrayUtils.step(start, stop, 1);
    }

    public static int[] step(int start, int stop, int step) {
        return ArrayUtils.step(start, stop, step);
    }
}
