package jetbrick.template;

import java.io.*;
import java.util.Map;
import jetbrick.template.parser.*;
import jetbrick.template.parser.code.Code;
import jetbrick.template.parser.grammer.JetTemplateLexer;
import jetbrick.template.parser.grammer.JetTemplateParser;
import jetbrick.template.resource.Resource;
import jetbrick.template.runtime.*;
import jetbrick.template.utils.ExceptionUtils;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JetTemplate {
    private static final Logger log = LoggerFactory.getLogger(JetTemplate.class);

    private final JetEngine engine;
    private final Resource resource;
    private final String encoding;
    private final boolean reloadable;
    private final File javaSourceFile;
    private final File javaClassFile;
    private JetPage pageObject;

    protected JetTemplate(JetEngine engine, Resource resource) {
        this.engine = engine;
        this.resource = resource;
        this.encoding = engine.getConfig().getOutputEncoding();
        this.reloadable = engine.getConfig().isTemplateReloadable();
        this.javaSourceFile = engine.getJdkCompiler().getGenerateJavaSourceFile(resource.getQualifiedClassName());
        this.javaClassFile = engine.getJdkCompiler().getGenerateJavaClassFile(resource.getQualifiedClassName());

        // compile and load
        if (javaClassFile.lastModified() > resource.lastModified()) {
            loadClassFile();
        } else {
            compileAndLoadClass();
        }
    }

    protected void checkLastModified() {
        if (reloadable && javaClassFile.lastModified() < resource.lastModified()) {
            synchronized (this) {
                // double check
                if (javaClassFile.lastModified() < resource.lastModified()) {
                    compileAndLoadClass();
                }
            }
        }
    }

    // 从 disk 的缓存文件中读取
    private void loadClassFile() {
        log.info("Loading template class file: " + javaClassFile.getAbsolutePath());
        try {
            Class<?> cls = engine.getClassLoader().loadClass(resource.getQualifiedClassName());
            pageObject = (JetPage) cls.newInstance();
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    // 编译 source 为 class， 然后 load class
    private void compileAndLoadClass() {
        log.info("Loading template source file: " + resource.getAbsolutePath());

        // generateJavaSource
        String source = generateJavaSource(resource);
        if (log.isInfoEnabled() && engine.getConfig().isCompileDebug()) {
            StringBuilder sb = new StringBuilder(source.length() + 128);
            sb.append("generateJavaSource: ");
            sb.append(javaSourceFile.getAbsolutePath());
            sb.append("\n");
            sb.append("===========================================================================\n");
            sb.append(source);
            sb.append("\n");
            sb.append("===========================================================================");
            log.info(sb.toString());
        }
        // compile
        Class<?> cls = engine.getJdkCompiler().compile(resource.getQualifiedClassName(), source);
        log.debug("generateJavaClass: " + javaClassFile.getAbsolutePath());
        try {
            pageObject = (JetPage) cls.newInstance();
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    private String generateJavaSource(Resource resource) {
        char[] source = resource.getSource();
        ANTLRInputStream is = new ANTLRInputStream(source, source.length);
        is.name = resource.getAbsolutePath(); // set source file name, it will be displayed in error report.

        JetTemplateParser parser = new JetTemplateParser(new CommonTokenStream(new JetTemplateLexer(is)));
        parser.removeErrorListeners(); // remove ConsoleErrorListener
        parser.addErrorListener(new JetTemplateErrorListener());
        parser.setErrorHandler(new JetTemplateErrorStrategy());

        JetTemplateCodeVisitor visitor = new JetTemplateCodeVisitor(engine, engine.getVariableResolver(), parser, resource);
        Code code = parser.template().accept(visitor);
        return code.getSource();
    }

    public void render(Map<String, Object> context, Writer out) {
        JetWriter writer = JetWriter.create(out, encoding);
        render(new JetContext(this, null, context, writer));
    }

    public void render(Map<String, Object> context, OutputStream out) {
        JetWriter writer = JetWriter.create(out, encoding);
        render(new JetContext(this, null, context, writer));
    }

    // 给 #include 和 include 函数用。
    public void render(JetContext parentContext, Map<String, Object> parameters, JetWriter out) {
        render(new JetContext(this, parentContext, parameters, out));
    }

    private void render(JetContext context) {
        try {
            pageObject.render(context);
        } catch (Throwable e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public JetEngine getEngine() {
        return engine;
    }

    public String getName() {
        return resource.getName();
    }
}
