package jetbrick.template.parser.code;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要用于存储 expression_list 和 type_list
 */
public class SegmentListCode implements Code {
    private final List<SegmentCode> children;

    public SegmentListCode(int initialCapacity) {
        children = new ArrayList<SegmentCode>(initialCapacity);
    }

    public void addChild(SegmentCode code) {
        children.add(code);
    }

    public List<SegmentCode> getChildren() {
        return children;
    }

    public SegmentCode getChild(int i) {
        return children.get(i);
    }

    public int size() {
        return children.size();
    }

    @Override
    public String getSource() {
        StringBuilder sb = new StringBuilder(32);
        for (SegmentCode code : children) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(code.getSource());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getSource();
    }
}
