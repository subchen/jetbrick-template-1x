package jetbrick.template.runtime;

import java.io.StringWriter;
import java.util.*;
import jetbrick.template.JetTemplate;
import jetbrick.template.utils.ArrayUtils;
import jetbrick.template.utils.PathUtils;

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

    public static String include(JetContext context, String name) {
        String file = PathUtils.relativePath(context.getTemplate().getName(), name);
        JetTemplate template = context.getEngine().getTemplate(file);
        if (template == null) {
            throw new IllegalAccessError("TemplateNotFound: " + file);
        }
        StringWriter os = new StringWriter();
        JetWriter out = JetWriter.create(os, context.getEngine().getConfig().getOutputEncoding());
        template.render(context, out);
        return os.toString();
    }
}
