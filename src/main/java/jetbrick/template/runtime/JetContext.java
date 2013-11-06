package jetbrick.template.runtime;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;

/**
 * 父子模板数据共享的 Context
 */
public class JetContext {
    private final JetTemplate template;
    private final JetWriter out;
    private final Map<String, Object> globalContext; // 全局共享 context
    private final Map<String, Object> privateContext; // 私有 context

    public JetContext(JetTemplate template, JetContext parentContext, Map<String, Object> context, JetWriter out) {
        this.template = template;
        this.out = out;

        if (parentContext == null) {
            globalContext = new HashMap<String, Object>(context.size() + 16);
            globalContext.putAll(context);
            privateContext = null;
        } else {
            globalContext = parentContext.globalContext;
            privateContext = context;
        }
    }

    public JetEngine getEngine() {
        return template.getEngine();
    }

    public JetTemplate getTemplate() {
        return template;
    }

    public JetWriter getWriter() {
        return out;
    }

    public Object get(String name) {
        Object value = null;
        if (privateContext != null) {
            value = privateContext.get(name);
        }
        if (value == null) {
            value = globalContext.get(name);
        }
        return value;
    }

    // 支持子模板返回变量到父模板
    public void put(String name, Object value) {
        globalContext.put(name, value);
    }
}
