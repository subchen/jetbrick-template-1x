package jetbrick.template.runtime;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.JetEngine;

public class JetContext {
    private final JetEngine engine;
    private final JetWriter out;
    private final Map<String, Object> context;

    public JetContext(JetEngine engine, Map<String, Object> context, JetWriter out) {
        this.engine = engine;
        this.out = out;

        this.context = new HashMap<String, Object>(context.size() + 8);
        this.context.putAll(context);
    }

    public JetEngine getEngine() {
        return engine;
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
