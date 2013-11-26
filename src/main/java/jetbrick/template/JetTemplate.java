/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        JetConfig config = engine.getConfig();

        this.engine = engine;
        this.resource = resource;
        this.encoding = config.getOutputEncoding();
        this.reloadable = config.isTemplateReloadable();
        this.javaSourceFile = engine.getJdkCompiler().getGenerateJavaSourceFile(resource.getQualifiedClassName());
        this.javaClassFile = engine.getJdkCompiler().getGenerateJavaClassFile(resource.getQualifiedClassName());

        // compile and load
        if ((!config.isCompileAlways()) && javaClassFile.lastModified() > resource.lastModified()) {
            try {
                loadClassFile();
            } catch (Throwable e) {
                // 无法 load 的话，尝试重新编译
                log.warn(e.getClass().getName() + ": " + e.getMessage());
                log.warn("Try to recompile the template.");
                compileAndLoadClass();
            }
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
    private void loadClassFile() throws Exception {
        log.info("Loading template class file: " + javaClassFile.getAbsolutePath());
        Class<?> cls = engine.getClassLoader().loadClass(resource.getQualifiedClassName());

        // 判断编码匹配
        if (!encoding.equals(cls.getDeclaredField("$ENC").get(null))) {
            throw new IllegalStateException("The encoding of last compiled template class is not " + encoding);
        }

        pageObject = (JetPage) cls.newInstance();
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
        log.info("generateJavaClass: " + javaClassFile.getAbsolutePath());
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
        return code.toString();
    }

    public void render(Map<String, Object> context, Writer out) {
        JetContext ctx = new JetContext(context);
        JetWriter writer = JetWriter.create(out, encoding);
        render(new JetPageContext(this, ctx, writer));
    }

    public void render(Map<String, Object> context, OutputStream out) {
        JetContext ctx = new JetContext(context);
        JetWriter writer = JetWriter.create(out, encoding);
        render(new JetPageContext(this, ctx, writer));
    }

    public void render(JetContext context, Writer out) {
        JetWriter writer = JetWriter.create(out, encoding);
        render(new JetPageContext(this, context, writer));
    }

    public void render(JetContext context, OutputStream out) {
        JetWriter writer = JetWriter.create(out, encoding);
        render(new JetPageContext(this, context, writer));
    }

    public void render(JetContext context, JetWriter writer) {
        render(new JetPageContext(this, context, writer));
    }

    private void render(JetPageContext ctx) {
        try {
            pageObject.render(ctx);
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
