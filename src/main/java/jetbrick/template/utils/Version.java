package jetbrick.template.utils;

import java.security.CodeSource;

public final class Version {
    private static final String VERSION = getVersion(Version.class, "1.0.0");

    private Version() {
    }

    public static String getVersion() {
        return VERSION;
    }

    public static String getVersion(Class<?> cls) {
        return getVersion(cls, getVersion());
    }

    public static String getVersion(Class<?> cls, String defaultVersion) {
        try {
            // look up version from MANIFEST.MF
            String version = cls.getPackage().getImplementationVersion();
            if (StringUtils.isEmpty(version)) {
                version = cls.getPackage().getSpecificationVersion();
            }
            if (StringUtils.isEmpty(version)) {
                // look up version from jar file name
                // sample: jetbrick-template-1.0.2.jar
                CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
                if (codeSource != null) {
                    String file = codeSource.getLocation().getFile();
                    if (StringUtils.isNotEmpty(file) && file.endsWith(".jar")) {
                        file = file.substring(0, file.length() - 4);
                        int i = file.lastIndexOf('/');
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }
                        i = file.indexOf('-');
                        if (i >= 0) {
                            file = file.substring(i + 1);
                        }
                        while (file.length() > 0 && !Character.isDigit(file.charAt(0))) {
                            i = file.indexOf('-');
                            if (i >= 0) {
                                file = file.substring(i + 1);
                            } else {
                                break;
                            }
                        }
                        version = file;
                    }
                }
            }

            return StringUtils.isEmpty(version) ? defaultVersion : version;
        } catch (Throwable e) {
            return defaultVersion;
        }
    }

}
