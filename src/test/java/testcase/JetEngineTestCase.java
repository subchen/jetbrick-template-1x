package testcase;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.*;
import jetbrick.template.*;
import jetbrick.template.parser.support.ClassUtils;
import jetbrick.template.resource.loader.ClasspathResourceLoader;
import testcase.model.SimpleModel;
import testcase.model.User;

public class JetEngineTestCase {
    private static final JetEngine engine = getEngine();
    private static final Map<String, Object> context = getContext();

    private static JetEngine getEngine() {
        Properties config = new Properties();
        config.put(JetConfig.IMPORT_PACKAGES, "testcase.model.*");
        config.put(JetConfig.IMPORT_VARIABLES, "User user, Book book, SimpleModel model, int[] array");
        config.put(JetConfig.TEMPLATE_LOADER, ClasspathResourceLoader.class.getName());
        config.put(JetConfig.TEMPLATE_PATH, "/");
        return new JetEngine(config);
    }

    private static Map<String, Object> getContext() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("model", SimpleModel.newInstance());
        context.put("user", User.newInstance());
        context.put("book", User.newInstance().getBookList().get(0));
        context.put("array", new int[] { 1, 2, 3 });
        context.put("items", new String[] { "a", "b", "c" });
        return context;
    }

    private static void test(String name, OutputStream out) {
        JetTemplate template = engine.getTemplate(name);
        template.render(context, out);
    }

    public static void main(String[] args) throws Throwable {
        List<String> filelist = new ArrayList<String>();
        if (args.length == 0) {
            URL url = ClassUtils.getContextClassLoader().getResource("template");
            File path = new File(url.getFile());
            for (File file : path.listFiles()) {
                if (file.getName().endsWith(".jetx")) {
                    filelist.add("/template/" + file.getName());
                }
            }
        } else {
            for (String arg : args) {
                filelist.add("/template/" + arg);
            }
        }

        //System.in.read();
        long ts = System.currentTimeMillis();
        int n = 1;
        for (int i = 0; i < n; i++) {
            for (String name : filelist) {
                //System.out.println("processing: " + file);
                //OutputStream out = new FileOutputStream(new File(path, name + ".txt"));
                //OutputStream out = NullOutputStream.NULL_OUTPUT_STREAM;
                OutputStream out = System.out;
                test(name, out);
                //out.close();
                // Thread.sleep(1000);
                //System.in.read();
            }
        }
        System.out.println();
        System.out.println("total: " + (System.currentTimeMillis() - ts));
        System.out.println("avg: " + (System.currentTimeMillis() - ts) / n / filelist.size());
        //System.in.read();
    }
}
