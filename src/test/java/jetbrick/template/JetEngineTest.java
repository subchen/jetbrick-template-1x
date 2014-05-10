package jetbrick.template;

import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Assert;
import org.junit.Test;

public class JetEngineTest {

    @Test
    public void noerror() {
        JetEngine.create();
    }

    @Test
    public void notfound2() {
        try {
            JetEngine.create(new File("1.txt"));
        } catch (RuntimeException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                return;
            }
        }
        Assert.fail();
    }

}
