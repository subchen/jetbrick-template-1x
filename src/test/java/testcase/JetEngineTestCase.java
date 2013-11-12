package testcase;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import jetbrick.template.*;
import jetbrick.template.parser.support.ClassUtils;
import jetbrick.template.resource.loader.ClasspathResourceLoader;
import jetbrick.template.utils.IoUtils;
import jetbrick.template.utils.UnsafeByteArrayOutputStream;

public class JetEngineTestCase {
    private static final JetEngine engine = getJetEngine();
    private static final JetContext context = getJetContext();

    private static JetEngine getJetEngine() {
        Properties config = new Properties();
        config.put(JetConfig.IMPORT_PACKAGES, "testcase.model.*");
        config.put(JetConfig.IMPORT_VARIABLES, "User user, Book book");
        config.put(JetConfig.TEMPLATE_LOADER, ClasspathResourceLoader.class.getName());
        config.put(JetConfig.TEMPLATE_PATH, "/");
        return JetEngine.create(config);
    }

    private static JetContext getJetContext() {
        JetContext context = new JetContext();
        testcase.model.User user = testcase.model.User.newInstance();
        context.put("user", user);
        context.put("book", user.getBookList().get(0));
        context.put("array", new int[] { 1, 2, 3 });
        context.put("items", new String[] { "a", "b", "c" });
        return context;
    }

    private static void render(String name, OutputStream out) {
        JetTemplate template = engine.getTemplate(name);
        template.render(context, out);
    }

    private static void testSuccessfully() throws IOException {
        URL url = ClassUtils.getContextClassLoader().getResource("template");
        File path = new File(url.getFile());

        for (File file : path.listFiles()) {
            if (file.getName().endsWith(".jetx")) {
                String name = "/template/" + file.getName();

                UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
                render(name, out);
                String out_contents = out.toString("utf-8");

                if (name.endsWith("-ignore.jetx")) continue;

                File outputFile = new File(path, file.getName() + ".txt");
                String contents = new String(IoUtils.toCharArray(outputFile, "utf-8"));

                if (!contents.equals(out_contents)) {
                    System.err.println("ASSERT ERROR: " + name);
                    System.err.println("==========================================");
                    System.err.println(out_contents);
                    System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    System.err.println(contents);
                    System.exit(1);
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        if (args.length > 0) {
            render(args[0], System.out);
            return;
        }

        long ts = System.currentTimeMillis();

        testSuccessfully();

        System.out.println();
        System.out.println("total: " + (System.currentTimeMillis() - ts));
        //System.in.read();
    }
}
