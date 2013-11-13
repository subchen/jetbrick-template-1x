package jetbrick.template.runtime;

import java.io.IOException;
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

    public static int[] range(int start, int stop) {
        return ArrayUtils.range(start, stop, 1);
    }

    public static int[] range(int start, int stop, int step) {
        return ArrayUtils.range(start, stop, step);
    }

    // 读取子模板内容
    public static String include(JetPageContext ctx, String relativeName) throws IOException {
        return JetUtils.asIncludeContent(ctx, relativeName, null);
    }

    // 读取子模板内容
    public static String include(JetPageContext ctx, String relativeName, Map<String, Object> parameters) throws IOException {
        return JetUtils.asIncludeContent(ctx, relativeName, parameters);
    }

    // 读取纯文本内容
    public static String read(JetPageContext ctx, String relativeName) {
        return JetUtils.asReadContent(ctx, relativeName, null);
    }

    // 读取纯文本内容
    public static String read(JetPageContext ctx, String relativeName, String encoding) {
        return JetUtils.asReadContent(ctx, relativeName, encoding);
    }

    public static void debug(String format, Object... args) {
        JetUtils.debug(format, args);
    }
}
