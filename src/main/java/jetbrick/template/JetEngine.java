package jetbrick.template;

import java.util.Properties;
import jetbrick.template.compiler.JdkCompiler;
import jetbrick.template.compiler.JetClassLoader;
import jetbrick.template.parser.VariableResolver;
import jetbrick.template.resource.Resource;
import jetbrick.template.resource.SourceCodeResource;
import jetbrick.template.resource.loader.ResourceLoader;
import jetbrick.template.utils.*;

public class JetEngine {
    private final JetConfig config;
    private final ResourceLoader resourceLoader;
    private final VariableResolver resolver;
    private final JetClassLoader classLoader;
    private final JdkCompiler jdkCompiler;
    private final ConcurrentTemplateCache templateCache;

    public JetEngine(Properties properties) {
        this.config = new JetConfig(properties);
        this.resolver = createVariableResolver();
        this.resourceLoader = createResourceLoader();
        this.classLoader = new JetClassLoader(config.getCompilePath(), config.isTemplateReloadable());
        this.jdkCompiler = JdkCompiler.create(this.classLoader);
        this.templateCache = new ConcurrentTemplateCache(this);
    }

    public Resource getResource(String name) {
        name = PathUtils.getStandardizedName(name);
        return resourceLoader.load(name);
    }

    public JetTemplate getTemplate(String name) {
        name = PathUtils.getStandardizedName(name);
        JetTemplate template = templateCache.get(name);
        if (template != null) {
            template.checkLastModified();
        }
        return template;
    }

    // No cache for template that from plain source
    public JetTemplate getTemplateFromSource(String source) {
        Resource resource = new SourceCodeResource(source);
        return new JetTemplate(this, resource);
    }

    protected VariableResolver getVariableResolver() {
        return resolver;
    }

    protected JetClassLoader getClassLoader() {
        return classLoader;
    }

    protected JdkCompiler getJdkCompiler() {
        return jdkCompiler;
    }

    public JetConfig getConfig() {
        return config;
    }

    public String getVersion() {
        return Version.getVersion(getClass());
    }

    private VariableResolver createVariableResolver() {
        VariableResolver resolver = new VariableResolver();
        for (String pkg : config.getImportPackages()) {
            resolver.addImportPackage(pkg);
        }
        for (String tool : config.getImportMethods()) {
            resolver.addMethodClass(tool);
        }
        for (String function : config.getImportFunctions()) {
            resolver.addFunctionClass(function);
        }
        for (String variable : config.getImportVariables()) {
            int pos = variable.lastIndexOf(" ");
            String defination = variable.substring(0, pos);
            String id = variable.substring(pos + 1);
            resolver.addGlobalVariable(defination, id);
        }
        return resolver;
    }

    private ResourceLoader createResourceLoader() {
        try {
            ResourceLoader resourceLoader = (ResourceLoader) config.getTemplateLoader().newInstance();
            resourceLoader.initialize(config.getTemplatePath(), config.getInputEncoding());
            return resourceLoader;
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    static class ConcurrentTemplateCache extends ConcurrentCache<String, JetTemplate> {
        private final JetEngine engine;

        public ConcurrentTemplateCache(JetEngine engine) {
            this.engine = engine;
        }

        @Override
        public JetTemplate doGetValue(String name) {
            Resource resource = engine.getResource(name);
            if (resource == null) return null;
            return new JetTemplate(engine, resource);
        }
    }
}
