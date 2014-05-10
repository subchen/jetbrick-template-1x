package jetbrick.template;

import java.util.Properties;
import jetbrick.template.resource.loader.ClasspathResourceLoader;
import org.junit.Test;
import testcase.TagUtils;

public class PreCompiledTestCase {

    @Test
    public void preCompiled() throws Exception {
        Properties config = new Properties();
        config.put(JetConfig.COMPILE_STRATEGY, "precompile");
        config.put(JetConfig.IMPORT_PACKAGES, "testcase.model.*");
        config.put(JetConfig.IMPORT_VARIABLES, "User user, Book book");
        config.put(JetConfig.IMPORT_TAGS, TagUtils.class.getName());
        config.put(JetConfig.TEMPLATE_LOADER, ClasspathResourceLoader.class.getName());
        config.put(JetConfig.TEMPLATE_PATH, "/");
        JetEngine.create(config);
        Thread.sleep(5000);
    }
}
