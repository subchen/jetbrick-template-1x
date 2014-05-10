package jetbrick.template;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import jetbrick.template.resource.loader.ClasspathResourceLoader;
import org.junit.Assert;
import org.junit.Test;
import testcase.TagUtils;

public class AutoCompiledTestCase {

    @Test
    public void autoCompile() throws Exception {
        Properties config = new Properties();
        config.put(JetConfig.COMPILE_STRATEGY, "auto");
        config.put(JetConfig.IMPORT_PACKAGES, "testcase.model.*");
        config.put(JetConfig.IMPORT_VARIABLES, "User user, Book book");
        config.put(JetConfig.IMPORT_TAGS, TagUtils.class.getName());
        config.put(JetConfig.TEMPLATE_LOADER, ClasspathResourceLoader.class.getName());
        config.put(JetConfig.TEMPLATE_PATH, "/");
        config.put(JetConfig.COMPILE_DEBUG, "true");
        JetEngine engine = JetEngine.create(config);

        JetTemplate template = engine.getTemplate("/template/text-plain.jetx");
        StringWriter out = new StringWriter();
        template.render((Map<String, Object>) null, out);
        Assert.assertEquals("1234567890", out.toString());
    }
}
