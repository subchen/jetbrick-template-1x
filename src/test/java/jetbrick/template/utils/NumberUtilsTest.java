package jetbrick.template.utils;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilsTest {

    private static final int[] values = { 1, 3, 5, 7, 9, 8, 6, 4, 2, 0 };

    @Test
    public void formatNumber() {
        Assert.assertNull(NumberUtils.format(null));
        Assert.assertEquals(NumberUtils.format(123), "123.00");
        Assert.assertEquals(NumberUtils.format(1234.5), "1,234.50");
        Assert.assertEquals(NumberUtils.format(1234.5555), "1,234.56");
        Assert.assertEquals(NumberUtils.format(1234.5555, "###0"), "1235");
    }

    @Test
    public void sum() {
        Assert.assertEquals(NumberUtils.sum(null), 0);
        Assert.assertEquals(NumberUtils.sum(new int[0]), 0);
        Assert.assertEquals(NumberUtils.sum(values), 45);
    }

    @Test
    public void avg() {
        Assert.assertEquals(NumberUtils.avg(null), 0);
        Assert.assertEquals(NumberUtils.avg(new int[0]), 0);
        Assert.assertEquals(NumberUtils.avg(values), 4);
    }

    @Test
    public void max() {
        Assert.assertEquals(NumberUtils.max(null), 0);
        Assert.assertEquals(NumberUtils.max(new int[0]), 0);
        Assert.assertEquals(NumberUtils.max(values), 9);
    }

    @Test
    public void min() {
        Assert.assertEquals(NumberUtils.min(null), 0);
        Assert.assertEquals(NumberUtils.min(new int[0]), 0);
        Assert.assertEquals(NumberUtils.min(values), 0);
    }
}
