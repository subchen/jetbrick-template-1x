package jetbrick.template;

import java.util.Properties;
import jetbrick.template.utils.UnsafeCharArrayWriter;
import org.junit.Assert;
import org.junit.Test;

public class NoneCompileTestCase {

    @Test
    public void noneCompiled() {
        Properties config = new Properties();
        config.put(JetConfig.COMPILE_STRATEGY, "none");
        JetEngine engine = JetEngine.create(config);
        JetTemplate template = engine.getTemplate("/template/text-plain.jetx");
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        JetContext context = new JetContext();
        template.render(context, out);
        System.out.println(out.toString());

        // 在 none 的情况下，source code 还是要编译的
        template = engine.createTemplate("你好");
        out = new UnsafeCharArrayWriter();
        template.render(new JetContext(), out);
        System.out.println(out.toString());
        Assert.assertEquals(out.toString(), "你好");
    }
}
