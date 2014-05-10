package jetbrick.template;

import java.util.Properties;
import org.junit.Test;

public class AutoScanTestCase {

    @Test
    public void scanPkg() throws Exception {
        Properties config = new Properties();
        config.put(JetConfig.IMPORT_PACKAGES, "testcase.model.*");
        config.put(JetConfig.IMPORT_VARIABLES, "User user, Book book");
        config.put(JetConfig.IMPORT_AUTOSCAN, "true");
        config.put(JetConfig.IMPORT_AUTOSCAN_PACKAGES, "testcase");
        JetEngine.create(config);
    }

    @Test
    public void scanAll() throws Exception {
        Properties config = new Properties();
        config.put(JetConfig.IMPORT_PACKAGES, "testcase.model.*");
        config.put(JetConfig.IMPORT_VARIABLES, "User user, Book book");
        config.put(JetConfig.IMPORT_AUTOSCAN, "true");
        JetEngine.create(config);
    }
}
