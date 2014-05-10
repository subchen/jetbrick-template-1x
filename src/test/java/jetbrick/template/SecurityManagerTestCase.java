package jetbrick.template;

import java.security.AccessControlException;
import java.util.Properties;
import jetbrick.template.parser.JetSecurityManagerImpl;
import jetbrick.template.utils.UnsafeCharArrayWriter;
import org.junit.Assert;
import org.junit.Test;

public class SecurityManagerTestCase {
    private final JetEngine engine;

    public SecurityManagerTestCase() {
        Properties config = new Properties();
        config.put(JetConfig.SECURITY_MANAGER, JetSecurityManagerImpl.class.getName());
        config.put(JetConfig.SECURITY_MANAGER_NAMELIST, "-java.util, +java.util.Date, -java.util.Date.getTime, -java.lang.System.exit, -java.lang.Integer.MAX_VALUE");
        engine = JetEngine.create(config);
    }

    @Test
    public void pkgAccess() throws Exception {
        try {
            JetTemplate template = engine.createTemplate("${new HashMap()}");
            UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
            template.render(new JetContext(), out);
        } catch (AccessControlException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void classAccess() throws Exception {
        JetTemplate template = engine.createTemplate("${new Date()}");
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        template.render(new JetContext(), out);
    }

    @Test
    public void methodAccess() throws Exception {
        try {
            JetTemplate template = engine.createTemplate("${new Date().time}");
            UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
            template.render(new JetContext(), out);
        } catch (AccessControlException e) {
            return;
        }
        Assert.fail();
    }

    @Test
    public void fieldAccess() throws Exception {
        try {
            JetTemplate template = engine.createTemplate("${@Integer.MAX_VALUE}");
            UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
            template.render(new JetContext(), out);
        } catch (AccessControlException e) {
            return;
        }
        Assert.fail();
    }
}
