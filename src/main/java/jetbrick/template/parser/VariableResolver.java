/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
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
package jetbrick.template.parser;

import java.lang.reflect.*;
import java.util.*;
import jetbrick.template.JetContext;
import jetbrick.template.parser.support.MethodFinderUtils;
import jetbrick.template.parser.support.TypedKlass;
import jetbrick.template.runtime.*;
import jetbrick.template.utils.ClassLoaderUtils;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.finder.PackagesFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariableResolver {
    private static final Logger log = LoggerFactory.getLogger(VariableResolver.class);

    private Set<String> importedPackageList = new LinkedHashSet<String>(); // 全局 import 的 Package
    private Map<String, Class<?>> importedClassMap = Collections.synchronizedMap(new HashMap<String, Class<?>>(64)); // 全局 import 独立的 Class
    private Map<String, TypedKlass> variableMap = new HashMap<String, TypedKlass>(); // 全局定义的变量
    private Map<String, List<Method>> methodMap1 = new HashMap<String, List<Method>>(64); // 全局导入的 method 类
    private Map<String, List<Method>> methodMap2 = new HashMap<String, List<Method>>(32); // 全局导入的 method 类 （带 JetPageContext）
    private Map<String, List<Method>> functionMap1 = new HashMap<String, List<Method>>(32); // 全局导入的 function 类
    private Map<String, List<Method>> functionMap2 = new HashMap<String, List<Method>>(); // 全局导入的 function 类 （带 JetPageContext）
    private Map<String, List<Method>> tagMap = new HashMap<String, List<Method>>(); // 全局导入的 tag 类 （带 JetTagContext）

    private static final Map<String, Member> bean_field_cache = Collections.synchronizedMap(new HashMap<String, Member>(64));
    private static final Map<String, Method> bean_method_cache = Collections.synchronizedMap(new HashMap<String, Method>(128));
    private static final Map<String, Constructor<?>> bean_constructor_cache = Collections.synchronizedMap(new HashMap<String, Constructor<?>>());
    private static final Map<String, Method> static_tool_method_cache = Collections.synchronizedMap(new HashMap<String, Method>(128));
    private static final Map<String, Method> static_function_cache = Collections.synchronizedMap(new HashMap<String, Method>(64));
    private static final Map<String, Method> static_tag_method_cache = Collections.synchronizedMap(new HashMap<String, Method>());
    private static final Map<String, Field> static_field_cache = Collections.synchronizedMap(new HashMap<String, Field>(16));
    private static final Map<String, Method> static_method_cache = Collections.synchronizedMap(new HashMap<String, Method>(16));

    public VariableResolver() {
        addImportPackage("java.lang");
        addImportPackage("java.util");
        //
        addMethodClass(JetMethods.class.getName());
        addFunctionClass(JetFunctions.class.getName());
        addTagClass(JetTags.class.getName());
    }

    public void addImportPackage(String pkg) {
        if (pkg.endsWith(".**")) {
            log.info("import package: {}", pkg);

            pkg = pkg.substring(0, pkg.length() - 3);
            Set<String> pkgs = PackagesFinder.getPackages(pkg);
            pkgs.add(pkg); // add self
            for (String subpkg : pkgs) {
                if (importedPackageList.add(subpkg)) {
                    log.info("found sub package: {}.*", subpkg);
                }
            }
            return;
        }

        if (pkg.endsWith(".*")) {
            pkg = pkg.substring(0, pkg.length() - 2);
        }
        if (importedPackageList.add(pkg)) {
            log.info("import package: {}.*", pkg);
        }
    }

    public void addImportClass(String klassName) {
        try {
            Class<?> klass = ClassLoaderUtils.loadClass(klassName);
            if (importedClassMap.put(klass.getSimpleName(), klass) == null) {
                log.info("import class: " + klass.getName());
            }
        } catch (ClassNotFoundException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public void addGlobalVariable(String klassName, String name) {
        TypedKlass klass = resolveTypedKlass(klassName);
        if (klass == null || klass == TypedKlass.NULL) {
            throw new RuntimeException("ClassNotFoundException: " + klassName);
        }
        if (variableMap.put(name, klass) == null) {
            log.info("add variable: {} {}", klass.getSource(), name);
        }
    }

    public void addMethodClass(String klassName) {
        Class<?> klass = resolveClass(klassName);
        if (klass == null) {
            throw new IllegalStateException("Cannot resolve class: " + klassName);
        }
        addMethodClass(klass);
    }

    public void addMethodClass(Class<?> klass) {
        for (Method method : klass.getMethods()) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                continue;
            }
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                String name = method.getName();

                List<Method> list;
                if (parameterTypes.length > 1 && JetPageContext.class.equals(parameterTypes[1])) {
                    list = methodMap2.get(name);
                    if (list == null) {
                        list = new ArrayList<Method>(4);
                        methodMap2.put(name, list);
                    }
                } else {
                    list = methodMap1.get(name);
                    if (list == null) {
                        list = new ArrayList<Method>(4);
                        methodMap1.put(name, list);
                    }
                }
                list.add(method);
            }
        }
        log.info("add method class: {}", klass.getName());
    }

    public void addFunctionClass(String klassName) {
        Class<?> klass = resolveClass(klassName);
        if (klass == null) {
            throw new IllegalStateException("Cannot resolve class: " + klassName);
        }
        addFunctionClass(klass);
    }

    public void addFunctionClass(Class<?> klass) {
        for (Method method : klass.getMethods()) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                String name = method.getName();

                List<Method> list;
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length > 0 && JetPageContext.class.equals(parameterTypes[0])) {
                    list = functionMap2.get(name);
                    if (list == null) {
                        list = new ArrayList<Method>(4);
                        functionMap2.put(name, list);
                    }
                } else {
                    list = functionMap1.get(name);
                    if (list == null) {
                        list = new ArrayList<Method>(4);
                        functionMap1.put(name, list);
                    }
                }
                list.add(method);
            }
        }
        log.info("add function class: {}", klass.getName());
    }

    public void addTagClass(String klassName) {
        Class<?> klass = resolveClass(klassName);
        if (klass == null) {
            throw new IllegalStateException("Cannot resolve class: " + klassName);
        }
        addTagClass(klass);
    }

    public void addTagClass(Class<?> klass) {
        for (Method method : klass.getMethods()) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                if (!Void.TYPE.equals(method.getReturnType())) {
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length > 0 && JetTagContext.class.equals(parameterTypes[0])) {
                    String name = method.getName();
                    List<Method> list = tagMap.get(name);
                    if (list == null) {
                        list = new ArrayList<Method>(4);
                        tagMap.put(name, list);
                    }
                    list.add(method);
                }
            }
        }
        log.info("add tag class: {}", klass.getName());
    }

    // 根据名称，查找到对应的class (支持完整的泛型声明)
    public TypedKlass resolveTypedKlass(String klassName) {
        if (klassName == null) return null;

        try {
            return new TypedKlassParser(klassName.toCharArray(), this).asTypedKlass();
        } catch (Throwable e) {
            return null;
        }
    }

    // 根据名称，查找到对应的class
    public Class<?> resolveClass(String klassName) {
        return doGetClass(klassName);
    }

    private Class<?> doGetClass(String klassName) {
        try {
            return ClassLoaderUtils.loadClass(klassName);
        } catch (ClassNotFoundException e) {
        }

        // 先查单个类的导入情况
        Class<?> klass = importedClassMap.get(klassName);
        if (klass != null) return klass;

        // 只对类似 String 或者 Map.Entry 这样的才尝试进行包名补齐
        int lpos = klassName.indexOf('.');
        if (lpos < 0 || lpos == klassName.lastIndexOf('.')) {
            if (lpos > 0) {
                // 内部类
                klassName = klassName.replace('.', '$');
            }
            for (String pkg : importedPackageList) {
                try {
                    klass = ClassLoaderUtils.loadClass(pkg + "." + klassName);
                    importedClassMap.put(klassName, klass); // cache for speed
                    return klass;
                } catch (ClassNotFoundException e) {
                }
            }
        }
        return null;
    }

    public TypedKlass resolveVariable(String identifier) {
        TypedKlass klass = variableMap.get(identifier);
        if (klass == null) {
            klass = TypedKlass.Object;
        }
        return klass;
    }

    // 找到对应的属性Get方法
    public Member resolveProperty(Class<?> beanClass, String name) {
        String key = getPrivateCacheKeyName(beanClass.getName(), name, null);

        // lookup cache
        Member member = bean_field_cache.get(key);
        if (member == null) {
            synchronized (bean_field_cache) {
                member = doResolveProperty(beanClass, name);
                if (member != null) {
                    bean_field_cache.put(key, member);
                }
            }
        }
        return member;
    }

    private Member doResolveProperty(Class<?> beanClass, String name) {
        // public method
        Method[] methods = beanClass.getMethods();

        // getXXX() or isXXX()
        String suffix = name.substring(0, 1).toUpperCase() + name.substring(1);
        String method_get = "get" + suffix;
        String method_is = "is" + suffix;
        for (Method method : methods) {
            String methodName = method.getName();
            if (method_get.equals(methodName) || method_is.equals(methodName)) {
                if (method.getParameterTypes().length == 0) {
                    return method;
                }
            }
        }

        // public field
        try {
            return beanClass.getField(name);
        } catch (NoSuchFieldException e) {
        }

        try {
            // get(Object) for Map
            if (Map.class.isAssignableFrom(beanClass)) {
                return beanClass.getMethod("get", Object.class);
            }
            // get(String) for JetContext
            if (JetContext.class.isAssignableFrom(beanClass)) {
                return beanClass.getMethod("get", String.class);
            }
        } catch (Exception e) {
            throw ExceptionUtils.uncheck(e);
        }

        return null;
    }

    // 找到对应的方法 bean.method(...)
    public Method resolveMethod(Class<?> beanClass, String methodName, Class<?>[] parameterTypes) {
        String key = getPrivateCacheKeyName(beanClass.getName(), methodName, parameterTypes);

        // lookup cache
        Method method = bean_method_cache.get(key);
        if (method == null) {
            synchronized (bean_method_cache) {
                method = MethodFinderUtils.lookupMethod(beanClass, methodName, parameterTypes);
                if (method != null) {
                    bean_method_cache.put(key, method);
                }
            }
        }
        return method;
    }

    // 找到对应的方法 Tool.method(bean, ...)
    public Method resolveToolMethod(Class<?> beanClass, String methodName, Class<?>[] parameterTypes) {
        List<Method> methods = methodMap1.get(methodName);
        if (methods == null) return null;

        Class<?>[] targetParameterTypes = new Class<?>[1 + parameterTypes.length];
        targetParameterTypes[0] = beanClass;
        for (int i = 0; i < parameterTypes.length; i++) {
            targetParameterTypes[i + 1] = parameterTypes[i];
        }

        String key = getPrivateCacheKeyName(null, methodName, targetParameterTypes);

        // lookup cache
        Method method = static_tool_method_cache.get(key);
        if (method == null) {
            synchronized (static_tool_method_cache) {
                method = MethodFinderUtils.lookupBestMethod(methods, methodName, targetParameterTypes);
                if (method != null) {
                    static_tool_method_cache.put(key, method);
                }
            }
        }
        return method;
    }

    // 找到对应的方法 Tool.method(bean, JetPageContext, ...)
    public Method resolveToolMethod_advanced(Class<?> beanClass, String methodName, Class<?>[] parameterTypes) {
        List<Method> methods = methodMap2.get(methodName);
        if (methods == null) return null;

        Class<?>[] targetParameterTypes = new Class<?>[2 + parameterTypes.length];
        targetParameterTypes[0] = beanClass;
        targetParameterTypes[1] = JetPageContext.class;
        for (int i = 0; i < parameterTypes.length; i++) {
            targetParameterTypes[i + 2] = parameterTypes[i];
        }

        String key = getPrivateCacheKeyName(null, methodName, targetParameterTypes);

        // lookup cache
        Method method = static_tool_method_cache.get(key);
        if (method == null) {
            synchronized (static_tool_method_cache) {
                method = MethodFinderUtils.lookupBestMethod(methods, methodName, targetParameterTypes);
                if (method != null) {
                    static_tool_method_cache.put(key, method);
                }
            }
        }
        return method;
    }

    // 找到对应的方法 function(...)
    public Method resolveFunction(String methodName, Class<?>[] parameterTypes) {
        List<Method> methods = functionMap1.get(methodName);
        if (methods == null) return null;

        String key = getPrivateCacheKeyName(null, methodName, parameterTypes);

        // lookup cache
        Method method = static_function_cache.get(key);
        if (method == null) {
            synchronized (static_function_cache) {
                method = MethodFinderUtils.lookupBestMethod(methods, methodName, parameterTypes);
                if (method != null) {
                    static_function_cache.put(key, method);
                }
            }
        }
        return method;
    }

    // 找到对应的方法 function(JetPageContext, ...)
    public Method resolveFunction_advanced(String methodName, Class<?>[] parameterTypes) {
        List<Method> methods = functionMap2.get(methodName);
        if (methods == null) return null;

        Class<?>[] targetParameterTypes = new Class<?>[1 + parameterTypes.length];
        targetParameterTypes[0] = JetPageContext.class;
        for (int i = 0; i < parameterTypes.length; i++) {
            targetParameterTypes[i + 1] = parameterTypes[i];
        }

        String key = getPrivateCacheKeyName(null, methodName, targetParameterTypes);

        // lookup cache
        Method method = static_function_cache.get(key);
        if (method == null) {
            synchronized (static_function_cache) {
                method = MethodFinderUtils.lookupBestMethod(methods, methodName, targetParameterTypes);
                if (method != null) {
                    static_function_cache.put(key, method);
                }
            }
        }
        return method;
    }

    // 找到对应的构造函数
    public Constructor<?> resolveConstructor(Class<?> beanClass, Class<?>[] parameterTypes) {
        String key = getPrivateCacheKeyName(beanClass.getName(), "<init>", parameterTypes);

        // lookup cache
        Constructor<?> constructor = bean_constructor_cache.get(key);
        if (constructor == null) {
            synchronized (bean_constructor_cache) {
                constructor = MethodFinderUtils.lookupConstructor(beanClass, parameterTypes);
                if (constructor != null) {
                    bean_constructor_cache.put(key, constructor);
                }
            }
        }
        return constructor;
    }

    // 找到对应的Tag tag(...)
    public Method resolveTagMethod(String methodName, Class<?>[] parameterTypes) {
        List<Method> methods = tagMap.get(methodName);
        if (methods == null) return null;

        String key = getPrivateCacheKeyName(null, methodName, parameterTypes);

        // lookup cache
        Method method = static_tag_method_cache.get(key);
        if (method == null) {
            synchronized (static_tag_method_cache) {
                method = MethodFinderUtils.lookupBestMethod(methods, methodName, parameterTypes);
                if (method != null) {
                    static_tag_method_cache.put(key, method);
                }
            }
        }
        return method;
    }

    public Field resolveStaticField(Class<?> beanClass, String name) {
        String key = getPrivateCacheKeyName(beanClass.getName(), name, null);

        // lookup cache
        Field field = static_field_cache.get(key);
        if (field == null) {
            synchronized (static_field_cache) {
                try {
                    field = beanClass.getField(name);
                    if (field != null) {
                        if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                            static_field_cache.put(key, field);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return field;
    }

    public Method resolveStaticMethod(Class<?> beanClass, String name, Class<?>[] parameterTypes) {
        String key = getPrivateCacheKeyName(beanClass.getName(), name, parameterTypes);

        // lookup cache
        Method method = static_method_cache.get(key);
        if (method == null) {
            synchronized (static_method_cache) {
                try {
                    method = MethodFinderUtils.lookupMethod(beanClass, name, parameterTypes);
                    if (method != null) {
                        if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
                            static_method_cache.put(key, method);
                        }
                    }
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return method;
    }

    // 生成一个字符串作为 Method Cache 的 Key
    private static String getPrivateCacheKeyName(String clazzName, String methodName, Class<?>[] parameterTypes) {
        StringBuilder sb = new StringBuilder(32);
        if (clazzName != null) {
            sb.append(clazzName);
            sb.append('/');
        }
        sb.append(methodName);
        if (parameterTypes != null) {
            for (Class<?> type : parameterTypes) {
                sb.append('/');
                sb.append(type == null ? "<null>" : type.getName());
            }
        }
        return sb.toString();
    }
}
