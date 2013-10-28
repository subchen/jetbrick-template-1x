package jetbrick.template.runtime;

/**
 * 提供给 #for指令用的内部计数器，从1开始计数
 */
public class JetForStatus {
    private int index;

    public JetForStatus() {
        index = 0;
    }

    public JetForStatus inc() {
        index++;
        return this;
    }

    public boolean empty() {
        return index == 0;
    }

    /**
     * ${for.index}
     */
    public int getIndex() {
        return index;
    }
}
