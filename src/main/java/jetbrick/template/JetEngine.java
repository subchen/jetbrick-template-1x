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

import java.io.File;
import java.util.Properties;
import jetbrick.template.compiler.JdkCompiler;
import jetbrick.template.compiler.JetClassLoader;
import jetbrick.template.parser.VariableResolver;
import jetbrick.template.resource.Resource;
import jetbrick.template.resource.SourceCodeResource;
import jetbrick.template.resource.loader.ResourceLoader;
import jetbrick.template.utils.*;

public class JetEngine {
    public static final String VERSION = Version.getVersion(JetEngine.class);

    private final JetConfig config;
    private final ResourceLoader resourceLoader;
    private final VariableResolver resolver;
    private final JetClassLoader classLoader;
    private final JdkCompiler jdkCompiler;
    private final ConcurrentResourceCache resourceCache;
    private final ConcurrentTemplateCache templateCache;

    public static JetEngine create() {
        return new JetEngine(new JetConfig().loadClasspath(JetConfig.DEFAULT_CONFIG_FILE));
    }

    public static JetEngine create(File configFile) {
        return new JetEngine(new JetConfig().loadFile(configFile));
    }

    public static JetEngine create(Properties properties) {
        return new JetEngine(new JetConfig().load(properties));
    }

    protected JetEngine(JetConfig config) {
        this.config = config.build();
        this.resolver = createVariableResolver();
        this.resourceLoader = createResourceLoader();
        this.classLoader = new JetClassLoader(config.getCompilePath(), config.isTemplateReloadable());
        this.jdkCompiler = JdkCompiler.create(this.classLoader);
        this.resourceCache = new ConcurrentResourceCache();
        this.templateCache = new ConcurrentTemplateCache();
    }

    /**
     * 根据一个绝对路径，判断资源文件是否存在
     */
    public boolean lookupResource(String name) {
        name = PathUtils.getStandardizedName(name);
        return resourceCache.get(name) != null;
    }

    /**
     * 根据一个绝对路径，获取一个资源对象
     * @throws ResourceNotFoundException
     */
    public Resource getResource(String name) throws ResourceNotFoundException {
        name = PathUtils.getStandardizedName(name);
        Resource resource = resourceCache.get(name);
        if (resource == null) {
            throw new ResourceNotFoundException(name);
        }
        return resource;
    }

    /**
     * 根据一个绝对路径，获取一个模板对象
     * @throws ResourceNotFoundException
     */
    public JetTemplate getTemplate(String name) throws ResourceNotFoundException {
        name = PathUtils.getStandardizedName(name);
        JetTemplate template = templateCache.get(name);
        template.checkLastModified();
        return template;
    }

    /**
     * 直接从源代码中生成一个模板对象
     */
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

    /**
     * 获取模板配置
     */
    public JetConfig getConfig() {
        return config;
    }

    /**
     * 获取模板引擎的版本号
     */
    public String getVersion() {
        return VERSION;
    }

    private VariableResolver createVariableResolver() {
        VariableResolver resolver = new VariableResolver();
        for (String pkg : config.getImportPackages()) {
            resolver.addImportPackage(pkg);
        }
        for (String method : config.getImportMethods()) {
            resolver.addMethodClass(method);
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

    private class ConcurrentResourceCache extends ConcurrentCache<String, Resource> {
        @Override
        public Resource doGetValue(String name) {
            return JetEngine.this.resourceLoader.load(name);
        }
    }

    private class ConcurrentTemplateCache extends ConcurrentCache<String, JetTemplate> {
        @Override
        public JetTemplate doGetValue(String name) {
            Resource resource = JetEngine.this.getResource(name);
            return new JetTemplate(JetEngine.this, resource);
        }
    }
}
