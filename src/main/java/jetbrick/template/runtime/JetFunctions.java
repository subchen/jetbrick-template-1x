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

    public static int[] step(int start, int stop) {
        return ArrayUtils.step(start, stop, 1);
    }

    public static int[] step(int start, int stop, int step) {
        return ArrayUtils.step(start, stop, step);
    }

    // 读取子模板内容
    public static String include(JetRuntimeContext context, String relativeName) throws IOException {
        return JetUtils.asIncludeContent(context, relativeName, null);
    }

    // 读取子模板内容
    public static String include(JetRuntimeContext context, String relativeName, Map<String, Object> parameters) throws IOException {
        return JetUtils.asIncludeContent(context, relativeName, parameters);
    }

    // 读取纯文本内容
    public static String read(JetRuntimeContext context, String relativeName) {
        return JetUtils.asReadContent(context, relativeName, null);
    }

    // 读取纯文本内容
    public static String read(JetRuntimeContext context, String relativeName, String encoding) {
        return JetUtils.asReadContent(context, relativeName, encoding);
    }
}
