package testcase;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;

public class EncodingTestCase {

    public static void main(String[] args) {
        Properties config = new Properties();
        //config.put(JetConfig.COMPILE_TOOL, JdtCompiler.class.getName());
        JetEngine engine = JetEngine.create(config);

        JetTemplate template = engine.createTemplate("是否登录");
        StringWriter out = new StringWriter();
        template.render(new HashMap<String, Object>(), out);
        System.out.println(out.toString());
    }
}
