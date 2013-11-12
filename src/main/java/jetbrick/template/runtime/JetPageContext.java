package jetbrick.template.runtime;

import jetbrick.template.*;
import jetbrick.template.utils.PathUtils;

public class JetPageContext {
    private final JetTemplate template;
    private final JetWriter out;
    private final JetContext context;

    public JetPageContext(JetTemplate template, JetContext context, JetWriter out) {
        this.template = template;
        this.context = context;
        this.out = out;
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

    public JetContext getContext() {
        return context;
    }

    /**
     * 基于当前模板路径，转换子模板相对路径为绝对路径。
     */
    public String getAbsolutionName(String name) {
        return PathUtils.getAbsolutionName(template.getName(), name);
    }
}
