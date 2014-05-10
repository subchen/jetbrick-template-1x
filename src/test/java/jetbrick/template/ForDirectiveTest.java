package jetbrick.template;

import jetbrick.template.utils.UnsafeCharArrayWriter;
import org.junit.Assert;
import org.junit.Test;

public class ForDirectiveTest {
    private JetEngine engine = JetEngine.create();

    public enum MyEnum {
        aa, bb, cc, dd
    }

    @Test
    public void forEnum() {
        JetTemplate template = engine.createTemplate("#for(item: items)${item}#end");
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        JetContext context = new JetContext();
        context.put("items", MyEnum.class);
        template.render(context, out);
        Assert.assertEquals(out.toString(), "aabbccdd");
    }
}
