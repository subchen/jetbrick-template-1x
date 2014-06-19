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
        Assert.assertEquals("/path/file.ext", PathUtils.getStandardizedName("/path/file.ext"));
        Assert.assertEquals("/path/file.ext", PathUtils.getStandardizedName("\\path\\file.ext"));
        Assert.assertEquals("/file.ext", PathUtils.getStandardizedName("/path/../file.ext"));
        Assert.assertEquals("/path/file.ext", PathUtils.getStandardizedName("path\\.\\file.ext"));
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

        boolean IS_WINDOW = System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0;
        if (IS_WINDOW) {
            Assert.assertEquals("path/to", PathUtils.getStandardizedTemplateRoot("path/to", true));
        } else {
            Assert.assertEquals("/path/to", PathUtils.getStandardizedTemplateRoot("path/to", true));
        }
        Assert.assertEquals("/path/to", PathUtils.getStandardizedTemplateRoot("path/to/", false));
        Assert.assertEquals("/path/to", PathUtils.getStandardizedTemplateRoot("\\path\\to", true));
        Assert.assertEquals("/path/to", PathUtils.getStandardizedTemplateRoot("\\path\\to/", false));
    }

    @Test
    public void combinePathName() {
    }

    @Test
    public void getAbsolutionName() {
    }

}
