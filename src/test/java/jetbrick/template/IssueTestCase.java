package jetbrick.template;

import java.util.Properties;
import jetbrick.template.utils.UnsafeCharArrayWriter;
import org.junit.Assert;
import org.junit.Test;

public class IssueTestCase {
    private final JetEngine engine;

    public IssueTestCase() {
        Properties config = new Properties();
        engine = JetEngine.create(config);
    }

    @Test
    public void test100() throws Exception {
        String source = "#set(Map<String, String> map = {'name':'jetbrick'})\n";
        source += "${map.name.length()}";
        JetTemplate template = engine.createTemplate(source);
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        template.render(new JetContext(), out);
        Assert.assertEquals("8", out.toString());
    }

    @Test
    public void test100_2() throws Exception {
        String source = "#set(Map<String, Map<String, String>> map = {'me':{'name':'jetbrick'}})\n";
        source += "${map.me.name.length()}";
        JetTemplate template = engine.createTemplate(source);
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        template.render(new JetContext(), out);
        Assert.assertEquals("8", out.toString());
    }
}
