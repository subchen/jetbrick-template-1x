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
package jetbrick.template.parser.support;

import java.util.HashSet;
import java.util.Set;

public class NumberClassUtils {
    private static final Set<Class<?>> numeric_class_set; // 数字类型
    private static final Set<Class<?>> integer_class_set; // 整数类型

    public static boolean isNumbericClass(Class<?> klass) {
        return numeric_class_set.contains(klass);
    }

    public static boolean isIntegerClass(Class<?> klass) {
        return integer_class_set.contains(klass);
    }

    static {
        numeric_class_set = new HashSet<Class<?>>(14);
        numeric_class_set.add(Byte.TYPE);
        numeric_class_set.add(Short.TYPE);
        numeric_class_set.add(Character.TYPE);
        numeric_class_set.add(Integer.TYPE);
        numeric_class_set.add(Long.TYPE);
        numeric_class_set.add(Float.TYPE);
        numeric_class_set.add(Double.TYPE);
        numeric_class_set.add(Byte.class);
        numeric_class_set.add(Short.class);
        numeric_class_set.add(Character.class);
        numeric_class_set.add(Integer.class);
        numeric_class_set.add(Long.class);
        numeric_class_set.add(Float.class);
        numeric_class_set.add(Double.class);

        integer_class_set = new HashSet<Class<?>>(10);
        integer_class_set.add(Byte.TYPE);
        integer_class_set.add(Short.TYPE);
        integer_class_set.add(Character.TYPE);
        integer_class_set.add(Integer.TYPE);
        integer_class_set.add(Long.TYPE);
        integer_class_set.add(Byte.class);
        integer_class_set.add(Short.class);
        integer_class_set.add(Character.class);
        integer_class_set.add(Integer.class);
        integer_class_set.add(Long.class);
    }
}
