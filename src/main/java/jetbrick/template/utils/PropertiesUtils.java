package jetbrick.template.utils;

import java.io.*;
import java.util.Properties;

public final class PropertiesUtils {

    public static Properties load(InputStream is) {
        if (is == null) return null;

        try {
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            IoUtils.closeQuietly(is);
        }
    }

    public static Properties load(File file) {
        if (file == null) return null;
        try {
            return load(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
