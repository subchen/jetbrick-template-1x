package jetbrick.template;

import java.util.Properties;
import jetbrick.template.runtime.JetTagContext;
import jetbrick.template.utils.UnsafeCharArrayWriter;
import org.junit.Assert;
import org.junit.Test;

public class TagInForeachTest {
    private JetEngine engine = getJetEngine();

    public JetEngine getJetEngine() {
        Properties config = new Properties();
        config.put(JetConfig.IMPORT_TAGS, myTag.class.getName());
        config.put(JetConfig.COMPILE_DEBUG, "true");
        return JetEngine.create(config);
    }

    @Test
    public void tagInFor() {
        String source = "#for(int i: iterator(1,9))#tag testTag()${i}#end#end";
        JetTemplate template = engine.createTemplate(source);
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        JetContext context = new JetContext();
        template.render(context, out);
        Assert.assertEquals(out.toString(), "123456789");
    }

    public static class myTag {
        public static void testTag(JetTagContext ctx) {
            ctx.writeBodyContent();
        }
    }
}
