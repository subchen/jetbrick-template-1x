package jetbrick.template.runtime;

import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class JetUtilsTest {

    @Test
    public void asBoolean() {
        Assert.assertFalse(JetUtils.asBoolean(null));
        Assert.assertFalse(JetUtils.asBoolean(false));
        Assert.assertFalse(JetUtils.asBoolean(0));
        Assert.assertFalse(JetUtils.asBoolean('\0'));
        Assert.assertFalse(JetUtils.asBoolean(""));
        Assert.assertFalse(JetUtils.asBoolean(new int[0]));
        Assert.assertFalse(JetUtils.asBoolean(new ArrayList<Object>()));
        Assert.assertFalse(JetUtils.asBoolean(new HashMap<Object, Object>()));
        Assert.assertFalse(JetUtils.asBoolean(new ArrayList<Object>().iterator()));

        Assert.assertTrue(JetUtils.asBoolean(true));
        Assert.assertTrue(JetUtils.asBoolean(1));
        Assert.assertTrue(JetUtils.asBoolean('a'));
        Assert.assertTrue(JetUtils.asBoolean("a"));
        Assert.assertTrue(JetUtils.asBoolean(new int[] { 1 }));
        Assert.assertTrue(JetUtils.asBoolean(Arrays.asList(1, 2, 3)));
        Assert.assertTrue(JetUtils.asBoolean(JetUtils.asMap("a", 123)));
        Assert.assertTrue(JetUtils.asBoolean(Arrays.asList(1, 2, 3).iterator()));
        Assert.assertTrue(JetUtils.asBoolean(new Object()));
    }

    @Test
    public void asCompareWith() {
        Assert.assertEquals(0, JetUtils.asCompareWith(1, 1));
        Assert.assertEquals(-1, JetUtils.asCompareWith(1L, Double.valueOf(2.0)));
    }
}
