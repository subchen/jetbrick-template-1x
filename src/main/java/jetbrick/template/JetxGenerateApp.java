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
import jetbrick.template.resource.loader.FileSystemResourceLoader;
import jetbrick.template.utils.finder.TemplateFileFinder;

/**
 * 一个用来在外部进行预编译的工具.
 * @see {@link https://github.com/subchen/jetbrick-template/issues/49}
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public class JetxGenerateApp {
    private boolean all = false;
    private String configFile;
    private String sourcePath;
    private String compilePath;

    public JetxGenerateApp() {
    }

    private void options(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-all".equals(arg)) {
                all = true;
            } else if ("-src".equals(arg)) {
                if (++i < args.length) {
                    sourcePath = args[i];
                }
            } else if ("-d".equals(arg)) {
                if (++i < args.length) {
                    compilePath = args[i];
                }
            } else if ("-config".equals(arg)) {
                if (++i < args.length) {
                    configFile = args[i];
                }
            } else if ("-help".equals(arg)) {
                printHelp();
            }
        }

        if (sourcePath == null || compilePath == null || configFile == null) {
            printHelp();
        }
    }

    private void printHelp() {
        System.out.println("jar -jar jetbrick-template-<version>.jar [options]");
        System.out.println();
        System.out.println("options:");
        System.out.println("    -help            Print this help.");
        System.out.println("    -all             Compile all templates even if errors.");
        System.out.println("    -src <path>      Template root directory.");
        System.out.println("    -d <path>        Output directory.");
        System.out.println("    -config <file>   Config file for template.");
        System.exit(0);
    }

    private void execute() {
        JetConfig config = new JetConfig();
        config.loadFile(new File(configFile));
        config.load(JetConfig.TEMPLATE_LOADER, FileSystemResourceLoader.class.getName());
        config.load(JetConfig.TEMPLATE_PATH, sourcePath);
        config.load(JetConfig.COMPILE_PATH, compilePath);
        config.load(JetConfig.COMPILE_STRATEGY, "always");
        config.load(JetConfig.COMPILE_DEBUG, "false");

        JetEngine engine = new JetEngine(config);

        TemplateFileFinder finder = new TemplateFileFinder(config.getTemplateSuffix());
        finder.lookupFileSystem(new File(sourcePath), true);
        List<String> resources = finder.getResources();

        System.out.println("Find " + resources.size() + " templates to precompile ...");
        int succ = 0;
        int fail = 0;
        long ts = System.currentTimeMillis();
        for (String name : resources) {
            try {
                engine.getTemplate(name);
                succ++;
            } catch (Exception e) {
                fail++;
                e.printStackTrace();
                if (!all) {
                    System.err.println();
                    System.err.println("Failed to compile templates.");
                    System.err.println("Please add -all option to compile all templates.");
                    System.err.println("System exit.");
                    System.exit(1);
                }
            }
        }
        ts = System.currentTimeMillis() - ts;
        System.out.println("Completed precompile templates in " + ts + " ms, success = " + succ + ", failure = " + fail + ".");
    }

    public static void main(String[] args) {
        JetxGenerateApp app = new JetxGenerateApp();
        app.options(args);
        app.execute();
    }
}
