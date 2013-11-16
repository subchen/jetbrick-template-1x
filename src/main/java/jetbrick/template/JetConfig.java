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
import java.util.List;
import java.util.Properties;
import jetbrick.template.resource.loader.FileSystemResourceLoader;
import jetbrick.template.utils.ConfigSupport;
import jetbrick.template.utils.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetConfig extends ConfigSupport<JetConfig> {
    public static final String DEFAULT_CONFIG_FILE = "jetbrick-template.properties";

    public static final String IMPORT_PACKAGES = "import.packages";
    public static final String IMPORT_METHODS = "import.methods";
    public static final String IMPORT_FUNCTIONS = "import.functions";
    public static final String IMPORT_VARIABLES = "import.variables";
    public static final String INPUT_ENCODING = "input.encoding";
    public static final String OUTPUT_ENCODING = "output.encoding";
    public static final String TEMPLATE_LOADER = "template.loader";
    public static final String TEMPLATE_PATH = "template.path";
    public static final String TEMPLATE_RELOADABLE = "template.reloadable";
    public static final String COMPILE_DEBUG = "compile.debug";
    public static final String COMPILE_PATH = "compile.path";
    public static final String TRIM_DIRECTIVE_LINE = "trim.directive.line";

    private final Logger log = LoggerFactory.getLogger(JetConfig.class);

    private List<String> importPackages;
    private List<String> importMethods;
    private List<String> importFunctions;
    private List<String> importVariables;
    private String inputEncoding;
    private String outputEncoding;
    private Class<?> templateLoader;
    private String templatePath;
    private boolean templateReloadable;
    private boolean compileDebug;
    private String compilePath;
    private boolean trimDirectiveLine;

    public JetConfig() {
        // default config
        String defaultCompilePath = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();

        Properties config = new Properties();
        config.setProperty(INPUT_ENCODING, "utf-8");
        config.setProperty(OUTPUT_ENCODING, "utf-8");
        config.setProperty(TEMPLATE_LOADER, FileSystemResourceLoader.class.getName());
        config.setProperty(TEMPLATE_PATH, PathUtils.getCurrentPath());
        config.setProperty(TEMPLATE_RELOADABLE, "false");
        config.setProperty(COMPILE_DEBUG, "true");
        config.setProperty(COMPILE_PATH, defaultCompilePath);
        config.setProperty(TRIM_DIRECTIVE_LINE, "true");
        load(config);
    }

    @Override
    public JetConfig build() {
        super.build();

        // log
        if (log.isDebugEnabled()) {
            log.debug("Load template from \"" + templatePath + "\" by " + templateLoader + ".");
            if (templateReloadable) {
                log.debug("autoload on: template will automatically reload.");
            } else {
                log.debug("autoload off: template will NOT automatically reload.");
            }
        }
        return this;
    }

    public List<String> getImportPackages() {
        return importPackages;
    }

    public List<String> getImportMethods() {
        return importMethods;
    }

    public List<String> getImportFunctions() {
        return importFunctions;
    }

    public List<String> getImportVariables() {
        return importVariables;
    }

    public String getInputEncoding() {
        return inputEncoding;
    }

    public String getOutputEncoding() {
        return outputEncoding;
    }

    public Class<?> getTemplateLoader() {
        return templateLoader;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public boolean isTemplateReloadable() {
        return templateReloadable;
    }

    public boolean isCompileDebug() {
        return compileDebug;
    }

    public String getCompilePath() {
        return compilePath;
    }

    public boolean isTrimDirectiveLine() {
        return trimDirectiveLine;
    }
}
