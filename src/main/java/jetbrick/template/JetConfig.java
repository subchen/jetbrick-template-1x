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
import java.lang.reflect.Field;
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
    public static final String IMPORT_CLASSES = "import.classes";
    public static final String IMPORT_METHODS = "import.methods";
    public static final String IMPORT_FUNCTIONS = "import.functions";
    public static final String IMPORT_TAGS = "import.tags";
    public static final String IMPORT_AUTOSCAN = "import.autoscan";
    public static final String IMPORT_AUTOSCAN_PACKAGES = "import.autoscan.packages";
    public static final String IMPORT_VARIABLES = "import.variables";
    public static final String INPUT_ENCODING = "input.encoding";
    public static final String OUTPUT_ENCODING = "output.encoding";
    public static final String TEMPLATE_LOADER = "template.loader";
    public static final String TEMPLATE_PATH = "template.path";
    public static final String TEMPLATE_SUFFIX = "template.suffix";
    public static final String TEMPLATE_RELOADABLE = "template.reloadable";
    public static final String COMPILE_ALWAYS = "compile.always";
    public static final String COMPILE_DEBUG = "compile.debug";
    public static final String COMPILE_PATH = "compile.path";
    public static final String TRIM_DIRECTIVE_LINE = "trim.directive.line";
    public static final String TRIM_DIRECTIVE_COMMENTS = "trim.directive.comments";
    public static final String TRIM_DIRECTIVE_COMMENTS_PREFIX = "trim.directive.comments.prefix";
    public static final String TRIM_DIRECTIVE_COMMENTS_SUFFIX = "trim.directive.comments.suffix";

    private final Logger log = LoggerFactory.getLogger(JetConfig.class);

    private List<String> importPackages;
    private List<String> importClasses;
    private List<String> importMethods;
    private List<String> importFunctions;
    private List<String> importTags;
    private boolean importAutoscan;
    private List<String> importAutoscanPackages;
    private List<String> importVariables;
    private String inputEncoding;
    private String outputEncoding;
    private Class<?> templateLoader;
    private String templatePath;
    private String templateSuffix;
    private boolean templateReloadable;
    private boolean compileAlways;
    private boolean compileDebug;
    private String compilePath;
    private boolean trimDirectiveLine;
    private boolean trimDirectiveComments;
    private String trimDirectiveCommentsPrefix;
    private String trimDirectiveCommentsSuffix;

    public JetConfig() {
        // default config
        String defaultCompilePath = new File(System.getProperty("java.io.tmpdir"), "jetx").getAbsolutePath();

        Properties config = new Properties();
        config.setProperty(IMPORT_AUTOSCAN, "false");
        config.setProperty(INPUT_ENCODING, "utf-8");
        config.setProperty(OUTPUT_ENCODING, "utf-8");
        config.setProperty(TEMPLATE_LOADER, FileSystemResourceLoader.class.getName());
        config.setProperty(TEMPLATE_PATH, PathUtils.getCurrentPath());
        config.setProperty(TEMPLATE_SUFFIX, ".jetx");
        config.setProperty(TEMPLATE_RELOADABLE, "false");
        config.setProperty(COMPILE_ALWAYS, "true");
        config.setProperty(COMPILE_DEBUG, "false");
        config.setProperty(COMPILE_PATH, defaultCompilePath);
        config.setProperty(TRIM_DIRECTIVE_LINE, "true");
        config.setProperty(TRIM_DIRECTIVE_COMMENTS, "false");
        config.setProperty(TRIM_DIRECTIVE_COMMENTS_PREFIX, "<!--");
        config.setProperty(TRIM_DIRECTIVE_COMMENTS_SUFFIX, "-->");
        load(config);
    }

    /**
     * Fixed #61: avoid split in Map<String, String>
     */
    @Override
    protected String[] split(Field field, String value) {
        if ("importVariables".equals(field.getName())) {
            return split(value, "<", ">");
        } else {
            return value.split(",");
        }
    }

    @Override
    public JetConfig build() {
        super.build();

        // log
        if (log.isInfoEnabled()) {
            log.info("Load template from \"" + templatePath + "\" by " + templateLoader + ".");
            if (templateReloadable) {
                log.info("Auto loading on: template will automatically reload.");
            } else {
                log.info("Auto loading off: template will NOT automatically reload.");
            }
        }
        return this;
    }

    public List<String> getImportPackages() {
        return importPackages;
    }

    public List<String> getImportClasses() {
        return importClasses;
    }

    public List<String> getImportMethods() {
        return importMethods;
    }

    public List<String> getImportFunctions() {
        return importFunctions;
    }

    public List<String> getImportTags() {
        return importTags;
    }

    public boolean isImportAutoscan() {
        return importAutoscan;
    }

    public List<String> getImportAutoscanPackages() {
        return importAutoscanPackages;
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

    public String getTemplateSuffix() {
        return templateSuffix;
    }

    public boolean isTemplateReloadable() {
        return templateReloadable;
    }

    public boolean isCompileAlways() {
        return compileAlways;
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

    public boolean isTrimDirectiveComments() {
        return trimDirectiveComments;
    }

    public String getTrimDirectiveCommentsPrefix() {
        return trimDirectiveCommentsPrefix;
    }

    public String getTrimDirectiveCommentsSuffix() {
        return trimDirectiveCommentsSuffix;
    }
}
