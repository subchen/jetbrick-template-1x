package jetbrick.template;

import java.util.Date;
import java.util.Properties;
import jetbrick.template.utils.UnsafeCharArrayWriter;
import org.junit.Assert;
import org.junit.Test;

public class GlobalVariablesTestCase {
    private final JetEngine engine;

    public GlobalVariablesTestCase() {
        Properties config = new Properties();
        config.put(JetConfig.IMPORT_VARIABLES, "String copyright, Date today");
        config.put(JetConfig.GLOBAL_VARIABLES, GlobalVariables.class.getName());
        engine = JetEngine.create(config);
    }

    @Test
    public void found() throws Exception {
        JetTemplate template = engine.createTemplate("${copyright} - ${today.format('yyyy')}");
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        template.render(new JetContext(), out);
        Assert.assertEquals(out.toString(), "copyright@2000-2010 - 2015");
    }

    static class GlobalVariables implements JetGlobalVariables {
        @Override
        public Object get(JetContext context, String name) {
            if ("copyright".equals(name)) {
                return "copyright@2000-2010";
            } else if ("today".equals(name)) {
                return new Date();
            }
            return null;
        }
    }
}
