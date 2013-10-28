package jetbrick.template.parser.code;

import jetbrick.template.utils.StringUtils;

/**
 * 用于存储一行源代码
 */
public class LineCode implements Code {
    private final String source;

    public LineCode(String source, int indent) {
        this.source = StringUtils.repeat(' ', indent * INDENT_SIZE) + source;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return source;
    }
}
