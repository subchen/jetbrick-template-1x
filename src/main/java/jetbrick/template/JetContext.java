package jetbrick.template;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放模板的 context 数据, 支持 chain
 */
public class JetContext {
    private final JetContext parent;
    private final Map<String, Object> context;

    public JetContext() {
        this(null, null);
    }

    public JetContext(Map<String, Object> context) {
        this(null, context);
    }

    public JetContext(JetContext parent, Map<String, Object> context) {
        this.parent = parent;

        if (context == null) {
            this.context = new HashMap<String, Object>();
        } else {
            this.context = new HashMap<String, Object>(context);
        }
    }

    public Object get(String name) {
        Object value = context.get(name);
        if (value == null && parent != null) {
            value = parent.get(name);
        }
        return value;
    }

    // 支持子模板返回变量到父模板
    public void put(String name, Object value) {
        context.put(name, value);
        if (parent != null) {
            parent.put(name, value);
        }
    }

    public void putAll(Map<String, Object> context) {
        this.context.putAll(context);
        if (parent != null) {
            parent.putAll(context);
        }
    }
}
