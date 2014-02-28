/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
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
package jetbrick.template.utils;

import java.security.CodeSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据 class 所在的 jar 包，查找 MANIFEST.MF 来识别版本号。
 */
public final class Version {
    private static final String DEFAULT_VERSION = "1.0.0";

    public static String getVersion(Class<?> cls) {
        return getVersion(cls, DEFAULT_VERSION);
    }

    public static String getVersion(Class<?> cls, String defaultVersion) {
        try {
            // look up version from MANIFEST.MF
            String version = cls.getPackage().getImplementationVersion();
            if (version == null || version.length() == 0) {
                version = cls.getPackage().getSpecificationVersion();
            }
            if (version == null || version.length() == 0) {
                // look up version from jar file name
                // sample: jetbrick-template-1.0.2.jar
                CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
                if (codeSource != null) {
                    String file = codeSource.getLocation().getFile();
                    if (file != null) {
                        Matcher matcher = Pattern.compile("[\\-\\._][vV]?(\\d+([\\-\\._]\\d+)*)\\.jar$").matcher(file);
                        if (matcher.find()) {
                            version = matcher.group(1);
                            version = version.replaceAll("[\\-_]", ".");
                        }
                    }
                }
            }

            if (version == null || version.length() == 0) {
                version = defaultVersion;
            }
            return version;

        } catch (Throwable e) {
            return defaultVersion;
        }
    }
}
