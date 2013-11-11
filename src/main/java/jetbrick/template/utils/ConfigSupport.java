package jetbrick.template.utils;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import jetbrick.template.parser.support.ClassUtils;

@SuppressWarnings("unchecked")
public abstract class ConfigSupport<T extends ConfigSupport<?>> {

    public T loadFile(File file) {
        try {
            return load(new FileInputStream(file));
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public T loadClasspath(String classpath) {
        return load(ClassUtils.getContextClassLoader().getResourceAsStream(classpath));
    }

    public T load(InputStream is) {
        if (is == null) return (T) this;
        try {
            Properties config = new Properties();
            config.load(is);
            return load(config);
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            IoUtils.closeQuietly(is);
        }
    }

    public T load(Properties config) {
        if (config == null) return (T) this;

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) continue;
            field.setAccessible(true);

            String name = WordUtils.toSpecialName(field.getName(), '.');
            String value = config.getProperty(name);
            if (value == null) continue;

            Class<?> type = field.getType();

            try {
                if (List.class.isAssignableFrom(type)) {
                    List<Object> values = (List<Object>) field.get(this);
                    if (values == null) {
                        values = new ArrayList<Object>();
                        field.set(this, values);
                    }

                    type = String.class; // String is the default Class
                    // try to lookup generic type for List<?>
                    Type genericType = field.getGenericType();
                    if (genericType instanceof ParameterizedType) {
                        Type[] actualTypeArgs = ((ParameterizedType) genericType).getActualTypeArguments();
                        if (actualTypeArgs != null && actualTypeArgs.length > 0) {
                            type = (Class<?>) actualTypeArgs[0];
                        }
                    }
                    for (String val : value.split(",")) {
                        values.add(cast(val, type));
                    }
                } else {
                    field.set(this, cast(value, type));
                }
            } catch (Exception e) {
                throw ExceptionUtils.uncheck(e);
            }
        }
        return (T) this;
    }

    public T build() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) continue;

            field.setAccessible(true);
            if (List.class.isAssignableFrom(field.getType())) {
                try {
                    List<?> values = (List<?>) field.get(this);
                    if (values == null) {
                        values = Collections.EMPTY_LIST;
                    } else {
                        values = Collections.unmodifiableList(values);
                    }
                    field.set(this, values);
                } catch (Exception e) {
                    throw ExceptionUtils.uncheck(e);
                }
            }
        }
        return (T) this;
    }

    private Object cast(String value, Class<?> type) {
        value = value.trim();
        if (String.class.equals(type)) {
            return value;
        }
        if (value.length() == 0) {
            if (Boolean.TYPE.equals(type)) {
                return Boolean.FALSE;
            } else if (Integer.TYPE.equals(type)) {
                return Integer.valueOf(0);
            } else if (Long.TYPE.equals(type)) {
                return Long.valueOf(value);
            } else if (Float.TYPE.equals(type)) {
                return Float.valueOf(0);
            } else if (Double.TYPE.equals(type)) {
                return Double.valueOf(0);
            }
            return null;
        }
        if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
            return Boolean.valueOf(value);
        } else if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
            return Integer.valueOf(value);
        } else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
            return Long.valueOf(value);
        } else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
            return Float.valueOf(value);
        } else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
            return Double.valueOf(value);
        } else if (Class.class.equals(type)) {
            return ClassUtils.getClass(value);
        }
        return value;
    }
}
