package jetbrick.template.utils;

import org.junit.Assert;
import org.junit.Test;

public class PathUtilsTest {

    @Test
    public void getStandardizedName() {
        try {
            PathUtils.getStandardizedName(null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        Assert.assertEquals(PathUtils.getStandardizedName("/path/file.ext"), "/path/file.ext");
        Assert.assertEquals(PathUtils.getStandardizedName("\\path\\file.ext"), "/path/file.ext");
        Assert.assertEquals(PathUtils.getStandardizedName("/path/../file.ext"), "/file.ext");
        Assert.assertEquals(PathUtils.getStandardizedName("path\\.\\file.ext"), "/path/file.ext");
        try {
            PathUtils.getStandardizedName("../../file");
            Assert.fail();
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void getStandardizedTemplateRoot() {
        try {
            PathUtils.getStandardizedTemplateRoot(null, false);
            Assert.fail();
        } catch (IllegalArgumentException e) {
        }
        Assert.assertEquals(PathUtils.getStandardizedTemplateRoot("path/to", true), "path/to");
        Assert.assertEquals(PathUtils.getStandardizedTemplateRoot("path/to/", false), "/path/to");
        Assert.assertEquals(PathUtils.getStandardizedTemplateRoot("\\path\\to", true), "/path/to");
        Assert.assertEquals(PathUtils.getStandardizedTemplateRoot("\\path\\to/", false), "/path/to");
    }

    @Test
    public void combinePathName() {
    }

    @Test
    public void getAbsolutionName() {
    }

}
