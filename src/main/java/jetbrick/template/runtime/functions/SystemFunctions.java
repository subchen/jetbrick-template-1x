package jetbrick.template.runtime.functions;

import java.util.*;

public final class SystemFunctions {
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
}
