package jetbrick.template;

import jetbrick.template.parser.SyntaxErrorException;
import jetbrick.template.utils.UnsafeCharArrayWriter;
import org.junit.Assert;
import org.junit.Test;

public class SourceCodeTest {
    private final JetEngine engine = JetEngine.create();

    @Test
    public void plain() {
        JetTemplate template = engine.createTemplate("你好");
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        template.render(new JetContext(), out);
        Assert.assertEquals("你好", out.toString());
    }

    @Test
    public void expression() {
        JetTemplate template = engine.createTemplate("${1+2*3}");
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        template.render(new JetContext(), out);
        Assert.assertEquals("7", out.toString());
    }

    @Test
    public void foreachArray() {
        JetContext context = new JetContext();
        context.put("applications", new String[][] { { "1", "2", "3" }, { "a", "b", "c" } });
        StringBuilder sb = new StringBuilder();
        sb.append("#for(String[] apps: applications)\n");
        sb.append("  #for(String app: apps)\n");
        sb.append("    ${app}\n");
        sb.append("  #end\n");
        sb.append("#end\n");
        JetTemplate template = engine.createTemplate(sb.toString());
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        template.render(context, out);
        Assert.assertEquals("    1\n    2\n    3\n    a\n    b\n    c\n", out.toString());
    }

    @Test
    public void undefinedGet() {
        StringBuilder sb = new StringBuilder();
        sb.append("${obj.name}\n");
        try {
            engine.createTemplate(sb.toString());
            Assert.fail();
        } catch (SyntaxErrorException e) {
            Assert.assertTrue(e.getMessage().contains("The method getName() or isName() is undefined for the type java.lang.Object"));
        }
    }

    @Test
    public void undefinedGet2() {
        StringBuilder sb = new StringBuilder();
        sb.append("#define(List obj)\n");
        sb.append("${obj[0].name}\n");
        try {
            engine.createTemplate(sb.toString());
            Assert.fail();
        } catch (SyntaxErrorException e) {
            Assert.assertTrue(e.getMessage().contains("The method getName() or isName() is undefined for the type java.lang.Object"));
        }
    }
}
