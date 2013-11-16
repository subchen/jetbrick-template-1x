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
