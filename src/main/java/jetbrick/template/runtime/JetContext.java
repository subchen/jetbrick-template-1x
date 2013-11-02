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
    private final Map<String, Object> context;

    public JetContext(JetTemplate template, Map<String, Object> context, JetWriter out) {
        this.template = template;
        this.out = out;

        this.context = new HashMap<String, Object>(context.size() + 8);
        this.context.putAll(context);
    }

    public JetContext(JetTemplate template, JetContext parentContext, JetWriter out) {
        this.template = template;
        this.out = out;
        this.context = parentContext.context; // 全局共享 context
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
        return context.get(name);
    }

    // 支持子模板返回变量到父模板
    public void put(String name, Object value) {
        context.put(name, value);
    }
}
