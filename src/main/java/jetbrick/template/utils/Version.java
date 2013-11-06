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
