package jetbrick.template.utils.finder;

import java.io.InputStream;
import jetbrick.template.JetAnnotations;
import jetbrick.template.utils.ClassLoaderUtils;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationClassReaderTestCase {
    @Test
    public void test() {
        AnnotationClassReader f = new AnnotationClassReader();
        f.addAnnotation(JetAnnotations.Tags.class);
        InputStream is = ClassLoaderUtils.getContextClassLoader().getResourceAsStream("testcase/TagUtils.class");
        Assert.assertEquals(true, f.isAnnotationed(is));
    }
}
