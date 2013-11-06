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
    public static String include(JetContext context, String relativeName) throws IOException {
        return JetUtils.getIncludeContent(context, relativeName, null);
    }

    // 读取子模板内容
    public static String include(JetContext context, String relativeName, Map<String, Object> parameters) throws IOException {
        return JetUtils.getIncludeContent(context, relativeName, parameters);
    }

    // 读取纯文本内容
    public static String read(JetContext context, String relativeName) {
        return JetUtils.getFileContext(context, relativeName, null);
    }

    // 读取纯文本内容
    public static String read(JetContext context, String relativeName, String encoding) {
        return JetUtils.getFileContext(context, relativeName, encoding);
    }
}
