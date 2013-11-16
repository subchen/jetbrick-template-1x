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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodFinderUtils {
    /**
     * 根据参数类型，查找最匹配的方法。
     */
    public static Method lookupMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            // find matching method
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return lookupBestMethod(Arrays.asList(clazz.getMethods()), methodName, parameterTypes);
        }
    }

    public static Method lookupBestMethod(List<Method> methods, String methodName, Class<?>... parameterTypes) {
        Method best = null;
        for (Method method : methods) {
            if (!method.getName().equals(methodName)) continue;

            Class<?>[] types = method.getParameterTypes();
            if (isParameterTypesCampatible(types, parameterTypes, method.isVarArgs(), false)) {
                // 可能有多个方法与实际参数类型兼容。采用就近兼容原则。
                if (best == null) {
                    best = method;
                } else if (best.isVarArgs() && (!method.isVarArgs())) {
                    best = method; // 不可变参数的函数优先
                } else if ((!best.isVarArgs()) && method.isVarArgs()) {
                    // no change
                } else if (isParameterTypesCampatible(best.getParameterTypes(), types, best.isVarArgs(), method.isVarArgs())) {
                    best = method;
                }
            }
        }
        return best;
    }

    /**
     * 根据参数类型，查找最匹配的构造函数。
     */
    public static Constructor<?> lookupConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        try {
            // find matching constructor
            return clazz.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            Constructor<?> best = null;
            for (Constructor<?> constructor : clazz.getConstructors()) {
                Class<?>[] types = constructor.getParameterTypes();
                if (isParameterTypesCampatible(types, parameterTypes, constructor.isVarArgs(), false)) {
                    // 可能有多个方法与实际参数类型兼容。采用就近兼容原则。
                    if (best == null) {
                        best = constructor;
                    } else if (best.isVarArgs() && (!constructor.isVarArgs())) {
                        best = constructor; // 不可变参数的函数优先
                    } else if ((!best.isVarArgs()) && constructor.isVarArgs()) {
                        // no change
                    } else if (isParameterTypesCampatible(best.getParameterTypes(), types, best.isVarArgs(), constructor.isVarArgs())) {
                        best = constructor;
                    }
                }
            }
            return best;
        }
    }

    /**
     * 判断参数列表是否兼容, 支持可变参数
     */
    public static boolean isParameterTypesCampatible(Class<?>[] lhs, Class<?>[] rhs, boolean lhsVarArgs, boolean rhsVarArgs) {
        if (lhs == null) {
            return rhs == null || rhs.length == 0;
        }
        if (rhs == null) {
            return lhs.length == 0;
        }

        if (lhsVarArgs && rhsVarArgs) {
            if (lhs.length != rhs.length) {
                return false;
            }
            //校验前面的固定参数
            for (int i = 0; i < lhs.length - 1; i++) {
                if (!ClassUtils.isAssignable(lhs[i], rhs[i])) {
                    return false;
                }
            }
            // 校验最后一个可变参数
            Class<?> c1 = lhs[lhs.length - 1].getComponentType();
            Class<?> c2 = rhs[rhs.length - 1].getComponentType();
            if (!ClassUtils.isAssignable(c1, c2)) {
                return false;
            }
        } else if (lhsVarArgs) {
            if (lhs.length - 1 > rhs.length) {
                return false;
            }
            //校验前面的固定参数
            for (int i = 0; i < lhs.length - 1; i++) {
                if (!ClassUtils.isAssignable(lhs[i], rhs[i])) {
                    return false;
                }
            }
            // 校验最后一个可变参数
            Class<?> varType = lhs[lhs.length - 1].getComponentType();
            for (int i = lhs.length - 1; i < rhs.length; i++) {
                if (!ClassUtils.isAssignable(varType, rhs[i])) {
                    return false;
                }
            }
        } else {
            if (lhs.length != rhs.length) {
                return false;
            }
            for (int i = 0; i < lhs.length; i++) {
                if (!ClassUtils.isAssignable(lhs[i], rhs[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
