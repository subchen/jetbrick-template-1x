package jetbrick.template;

import org.junit.Assert;
import org.junit.Test;

public class JetConfigTest {

    @Test
    public void split() {
        JetConfig config = new JetConfig();
        config.load(JetConfig.IMPORT_VARIABLES, "Map<String, String> map,List<String> list,java.lang.String obj");
        config.load(JetConfig.IMPORT_PACKAGES, "java.io");
        config.build();
        Assert.assertEquals(3, config.getImportVariables().size());
        Assert.assertEquals(1, config.getImportPackages().size());
        Assert.assertEquals(0, config.getImportClasses().size());
    }

}
