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
package jetbrick.template.utils;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jetbrick.template.parser.support.ClassUtils;

@SuppressWarnings("unchecked")
public abstract class ConfigSupport<T extends ConfigSupport<T>> {
    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\$\\{([^}]*)\\}");

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
            String name = WordUtils.toPropertyName(field.getName());
            String value = config.getProperty(name);
            if (value == null) continue;
            setFieldValue(field, value);
        }
        return (T) this;
    }

    public T load(String name, String value) {
        try {
            String fieldName = WordUtils.toCamelCase(name);
            Field field = getClass().getDeclaredField(fieldName);
            setFieldValue(field, value);
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
        return (T) this;
    }

    protected void setFieldValue(Field field, String value) {
        if (field == null || Modifier.isFinal(field.getModifiers())) return;

        field.setAccessible(true);
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
                        Type actualType = actualTypeArgs[0];
                        if ((actualType instanceof Class)) {
                            type = (Class<?>) actualType;
                        } else if (actualType instanceof ParameterizedType) {
                            type = (Class<?>) ((ParameterizedType) actualType).getRawType();
                        }
                    }
                }
                if (value == null) return;

                for (String val : split(field, value)) {
                    val = val.trim();
                    if (val.length() > 0) {
                        values.add(cast(val, type));
                    }
                }
            } else {
                if (value != null) {
                    value = value.trim();
                }
                field.set(this, cast(value, type));
            }
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    protected String[] split(Field field, String value) {
        return value.split(",");
    }

    /**
     * 固化配置，对象变成只读。目前只要是处理 List as unmodifiableList
     */
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

    @SuppressWarnings("rawtypes")
    private Object cast(String value, Class<?> type) {
        if (value == null) {
            return value;
        }
        value = replaceValue(value);

        if (String.class.equals(type)) {
            return value;
        }
        if (value == null || value.length() == 0) {
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
            value = value.toLowerCase();
            return "true".equals(value) || "1".equals(value);
        } else if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
            return Integer.valueOf(value);
        } else if (Long.class.equals(type) || Long.TYPE.equals(type)) {
            return Long.valueOf(value);
        } else if (Float.class.equals(type) || Float.TYPE.equals(type)) {
            return Float.valueOf(value);
        } else if (Double.class.equals(type) || Double.TYPE.equals(type)) {
            return Double.valueOf(value);
        } else if (Date.class.equals(type)) {
            return DateUtils.asDate(value);
        } else if (Class.class.equals(type)) {
            return ClassUtils.getClass(value);
        } else if (type.isEnum()) {
            return Enum.valueOf((Class<Enum>) type, value);
        }
        return value;
    }

    // 根据 System.getProperty() 替换变量
    private String replaceValue(String value) {
        if (value == null) {
            return null;
        }
        if (value.indexOf('$') >= 0) {
            Matcher matcher = PARAMETER_PATTERN.matcher(value);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String val = matcher.group(1);
                val = System.getProperty(val);
                if (val == null) {
                    val = "";
                }
                matcher.appendReplacement(sb, Matcher.quoteReplacement(val));
            }
            matcher.appendTail(sb);
            value = sb.toString();
        }
        return value;
    }

    /**
     * 按照逗号分隔字符串，如果在 openChars 和 closeChars 之间的逗号，则不分割.
     */
    protected String[] split(String value, String openChars, String closeChars) {
        List<String> results = new ArrayList<String>();
        int mode = 0;
        int lastpos = 0;
        for (int i = 0, len = value.length(); i < len; i++) {
            char c = value.charAt(i);
            if (openChars.indexOf(c) >= 0) {
                mode++;
            } else if (closeChars.indexOf(c) >= 0) {
                mode--;
            } else if (c == ',' && mode == 0) {
                results.add(value.substring(lastpos, i));
                lastpos = i + 1;
            }
        }
        if (lastpos < value.length()) {
            results.add(value.substring(lastpos));
        }
        return results.toArray(new String[results.size()]);
    }
}
