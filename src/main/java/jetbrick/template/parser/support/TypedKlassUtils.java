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

import java.lang.reflect.*;

public class TypedKlassUtils {

    /**
     * 获取字段声明中获取泛型 Class.
     */
    public static TypedKlass getFieldTypedKlass(Field field) {
        if (field == null) return null;

        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            return doGetGenericType(type);
        } else {
            return TypedKlass.create(field.getType());
        }
    }

    /**
     * 获取方法声明中获取返回类型的泛型 Class.
     */
    public static TypedKlass getMethodReturnTypedKlass(Method method) {
        if (method == null) return null;

        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            return doGetGenericType(type);
        } else {
            return TypedKlass.create(method.getReturnType());
        }
    }

    private static TypedKlass doGetGenericType(Type actualType) {
        if (actualType instanceof Class) {
            // List<String>
            return TypedKlass.create((Class<?>) actualType);
        } else if (actualType instanceof TypeVariable) {
            // List<T extends Number> or List<T>
            Type[] bounds = ((TypeVariable<?>) actualType).getBounds();
            if (bounds != null && bounds.length == 1) {
                return doGetGenericType(bounds[0]);
            }
        } else if (actualType instanceof WildcardType) {
            // List<?>
            return TypedKlass.WildcharTypedKlass;
        } else if (actualType instanceof ParameterizedType) {
            // List<List<?>>
            ParameterizedType parameterizedType = (ParameterizedType) actualType;

            Class<?> klass = (Class<?>) parameterizedType.getRawType();
            TypedKlass[] typeArgs = TypedKlass.EMPTY_TYPE_ARGS;

            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length >= 0) {
                typeArgs = new TypedKlass[actualTypeArguments.length];
                for (int i = 0; i < actualTypeArguments.length; i++) {
                    typeArgs[i] = doGetGenericType(actualTypeArguments[i]);
                }
            }
            return TypedKlass.create(klass, typeArgs);
        }

        return TypedKlass.Object;
    }
}
