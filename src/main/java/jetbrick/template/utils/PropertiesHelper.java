package jetbrick.template.utils;

import java.util.Properties;

public class PropertiesHelper {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    protected Properties props;

    public PropertiesHelper(Properties props) {
        this.props = props;
    }

    public String[] getStringArray(String s) {
        String value = props.getProperty(s);
        if (value != null) {
            return value.split(",", -1);
        }
        return EMPTY_STRING_ARRAY;
    }

    public String getString(String s) {
        return props.getProperty(s);
    }

    public boolean getBoolean(String s) {
        String value = props.getProperty(s);
        if (value != null) {
            return "true".equalsIgnoreCase(value) || "1".equals(value) || "yes".equalsIgnoreCase(value);
        }
        return false;
    }

    public Class<?> getClass(String s) {
        String value = props.getProperty(s);
        if (value != null) {
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(value);
            } catch (ClassNotFoundException e) {
                throw ExceptionUtils.uncheck(e);
            }
        }
        return null;
    }
}
