package jetbrick.template.parser;

import java.lang.reflect.*;
import java.util.*;
import jetbrick.template.parser.support.*;
import jetbrick.template.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VariableResolver {
    private final Logger log = LoggerFactory.getLogger(VariableResolver.class);

    private Set<String> importedPackageList = new HashSet<String>(); // 全局 import 的包
    private Map<String, TypedKlass> variableMap = new HashMap<String, TypedKlass>(); // 全局定义的变量
    private Map<String, List<Method>> methodMap1 = new HashMap<String, List<Method>>(64); // 全局导入的 method 类
    private Map<String, List<Method>> methodMap2 = new HashMap<String, List<Method>>(32); // 全局导入的 method 类 （带 JetRuntimeContext）
    private Map<String, List<Method>> functionMap1 = new HashMap<String, List<Method>>(32); // 全局导入的 function 类
    private Map<String, List<Method>> functionMap2 = new HashMap<String, List<Method>>(); // 全局导入的 function 类 （带 JetRuntimeContext）

    private static final Map<String, Member> bean_field_cache = new WeakHashMap<String, Member>(64);
    private static final Map<String, Method> bean_method_cache = new WeakHashMap<String, Method>(128);
    private static final Map<String, Method> static_method_cache = new WeakHashMap<String, Method>(128);
    private static final Map<String, Method> static_function_cache = new WeakHashMap<String, Method>(64);
    private static final Map<String, Constructor<?>> bean_constructor_cache = new WeakHashMap<String, Constructor<?>>();

    public VariableResolver() {
        addImportPackage("java.lang");
        addImportPackage("java.util");
        //
        addMethodClass(JetMethods.class.getName());
        addFunctionClass(JetFunctions.class.getName());
    }

    public void addImportPackage(String pkg) {
        if (pkg.endsWith(".*")) {
            pkg = pkg.substring(0, pkg.length() - 2);
        } else if (pkg.endsWith(".")) {
            pkg = pkg.substring(0, pkg.length() - 1);
        }
        importedPackageList.add(pkg);

        log.info("import package: " + pkg + ".*");
    }

    public void addGlobalVariable(String klassName, String name) {
        TypedKlass klass = resolveTypedKlass(klassName);
        if (klass == null || klass == TypedKlass.NULL) {
            throw new RuntimeException("ClassNotFoundException: " + klassName);
        }
        variableMap.put(name, klass);
        log.info("add variable: " + klass.getSource() + " " + name);
    }

    public void addMethodClass(String klassName) {
        Class<?> klass = resolveClass(klassName);
        if (klass == null) {
            throw new RuntimeException("ClassNotFoundException: " + klassName);
        }
        for (Method method : klass.getMethods()) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                continue;
            }
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                String name = method.getName();

                List<Method> list;
                if (parameterTypes.length > 1 && JetRuntimeContext.class.equals(parameterTypes[1])) {
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
        log.info("add method class: " + klass.getName());
    }

    public void addFunctionClass(String klassName) {
        Class<?> klass = resolveClass(klassName);
        if (klass == null) {
            throw new RuntimeException("ClassNotFoundException: " + klassName);
        }
        for (Method method : klass.getMethods()) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                String name = method.getName();

                List<Method> list;
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length > 0 && JetRuntimeContext.class.equals(parameterTypes[0])) {
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
        log.info("add function class: " + klass.getName());
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
        Class<?> klass = ClassUtils.getClass(klassName);
        if (klass != null) return klass;

        // 只对类似 String 或者 Map.Entry 这样的才尝试进行包名补齐
        int lpos = klassName.indexOf('.');
        if (lpos < 0 || lpos != klassName.lastIndexOf('.')) {
            for (String pkg : importedPackageList) {
                klass = ClassUtils.getClass(pkg + "." + klassName);
                if (klass != null) return klass;
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
        // public field
        try {
            return beanClass.getField(name);
        } catch (NoSuchFieldException e) {
        }

        // public method
        Method[] methods = beanClass.getMethods();

        // getXXX()
        String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
        for (Method method : methods) {
            if (methodName.equals(method.getName()) && method.getParameterTypes().length == 0) {
                return method;
            }
        }
        // isXXX()
        methodName = "is" + name.substring(0, 1).toUpperCase() + name.substring(1);
        for (Method method : methods) {
            if (methodName.equals(method.getName()) && method.getParameterTypes().length == 0) {
                return method;
            }
        }

        // get(String) for map like...
        methodName = "get";
        for (Method method : methods) {
            if (methodName.equals(method.getName())) {
                Class<?>[] types = method.getParameterTypes();
                if (types.length == 1 && String.class.equals(types[0])) {
                    return method;
                }
            }
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
        Method method = static_method_cache.get(key);
        if (method == null) {
            synchronized (static_method_cache) {
                method = MethodFinderUtils.lookupBestMethod(methods, methodName, targetParameterTypes);
                if (method != null) {
                    static_method_cache.put(key, method);
                }
            }
        }
        return method;
    }

    // 找到对应的方法 Tool.method(bean, JetRuntimeContext, ...)
    public Method resolveToolMethod_advanced(Class<?> beanClass, String methodName, Class<?>[] parameterTypes) {
        List<Method> methods = methodMap2.get(methodName);
        if (methods == null) return null;

        Class<?>[] targetParameterTypes = new Class<?>[2 + parameterTypes.length];
        targetParameterTypes[0] = beanClass;
        targetParameterTypes[1] = JetRuntimeContext.class;
        for (int i = 0; i < parameterTypes.length; i++) {
            targetParameterTypes[i + 2] = parameterTypes[i];
        }

        String key = getPrivateCacheKeyName(null, methodName, targetParameterTypes);

        // lookup cache
        Method method = static_method_cache.get(key);
        if (method == null) {
            synchronized (static_method_cache) {
                method = MethodFinderUtils.lookupBestMethod(methods, methodName, targetParameterTypes);
                if (method != null) {
                    static_method_cache.put(key, method);
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

    // 找到对应的方法 function(JetRuntimeContext, ...)
    public Method resolveFunction_advanced(String methodName, Class<?>[] parameterTypes) {
        List<Method> methods = functionMap2.get(methodName);
        if (methods == null) return null;

        Class<?>[] targetParameterTypes = new Class<?>[1 + parameterTypes.length];
        targetParameterTypes[0] = JetRuntimeContext.class;
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
                sb.append(type.getName());
            }
        }
        return sb.toString();
    }
}
