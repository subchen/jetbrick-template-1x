package jetbrick.template;

import java.io.File;
import java.util.Properties;
import jetbrick.template.resource.loader.FileSystemResourceLoader;
import jetbrick.template.utils.PropertiesHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetConfig {
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
    private final String[] importPackages;
    private final String[] importMethods;
    private final String[] importFunctions;
    private final String[] importVariables;
    private final String inputEncoding;
    private final String outputEncoding;
    private final Class<?> templateLoader;
    private final String templatePath;
    private final boolean templateReloadable;
    private final boolean compileDebug;
    private final String compilePath;
    private final boolean trimDirectiveLine;

    protected JetConfig(Properties properties) {
        // default config
        String defaultCompilePath = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath();

        Properties config = new Properties();
        config.setProperty(INPUT_ENCODING, "utf-8");
        config.setProperty(OUTPUT_ENCODING, "utf-8");
        config.setProperty(TEMPLATE_LOADER, FileSystemResourceLoader.class.getName());
        config.setProperty(TEMPLATE_PATH, "");
        config.setProperty(TEMPLATE_RELOADABLE, "false");
        config.setProperty(COMPILE_DEBUG, "true");
        config.setProperty(COMPILE_PATH, defaultCompilePath);
        config.setProperty(TRIM_DIRECTIVE_LINE, "true");
        if (properties != null) {
            config.putAll(properties);
        }

        // set user config
        PropertiesHelper p = new PropertiesHelper(config);
        importPackages = p.getStringArray(IMPORT_PACKAGES);
        importMethods = p.getStringArray(IMPORT_METHODS);
        importFunctions = p.getStringArray(IMPORT_FUNCTIONS);
        importVariables = p.getStringArray(IMPORT_VARIABLES);
        inputEncoding = p.getString(INPUT_ENCODING);
        outputEncoding = p.getString(OUTPUT_ENCODING);
        templateLoader = p.getClass(TEMPLATE_LOADER);
        templatePath = p.getString(TEMPLATE_PATH);
        templateReloadable = p.getBoolean(TEMPLATE_RELOADABLE);
        compileDebug = p.getBoolean(COMPILE_DEBUG);
        compilePath = p.getString(COMPILE_PATH);
        trimDirectiveLine = p.getBoolean(TRIM_DIRECTIVE_LINE);

        // log
        if (log.isDebugEnabled()) {
            log.debug("Load template from \"" + templatePath + "\" by " + templateLoader + ".");
            if (templateReloadable) {
                log.debug("autoload on: template will automatically reload.");
            } else {
                log.debug("autoload off: template will NOT automatically reload.");
            }
        }
    }

    public String[] getImportPackages() {
        return importPackages;
    }

    public String[] getImportMethods() {
        return importMethods;
    }

    public String[] getImportFunctions() {
        return importFunctions;
    }

    public String[] getImportVariables() {
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
