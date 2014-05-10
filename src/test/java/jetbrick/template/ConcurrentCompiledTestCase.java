package jetbrick.template;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import jetbrick.template.utils.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class ConcurrentCompiledTestCase {

    @Test
    public void compile() throws Exception {
        Properties config = new Properties();
        //config.put(JetConfig.COMPILE_TOOL, JdtCompiler.class.getName());
        config.put(JetConfig.COMPILE_DEBUG, "true");
        final JetEngine engine = JetEngine.create(config);
        final String source = StringUtils.repeat("abc${'123'.asString().asInt().asDefault(0)}", 200);

        ThreadGroup g = new ThreadGroup("a");
        for (int i = 0; i < 10; i++) {
            new Thread(g, "t-" + i) {
                @Override
                public void run() {
                    JetTemplate template = engine.createTemplate(source);
                    StringWriter out = new StringWriter();
                    template.render((Map<String, Object>) null, out);
                    Assert.assertEquals(StringUtils.repeat("abc123", 200), out.toString());
                }
            }.start();
        }
        while (g.activeCount() > 0) {
            Thread.sleep(100);
        }
    }
}
