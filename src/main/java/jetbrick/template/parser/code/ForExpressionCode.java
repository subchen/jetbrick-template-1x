package jetbrick.template.parser.code;

import jetbrick.template.parser.support.TypedKlass;

/**
 * 专门用于存储 for_expression
 */
public class ForExpressionCode extends SegmentCode {
    private final String name; // 变量名

    public ForExpressionCode(TypedKlass typedKlass, String name, String source) {
        super(typedKlass, source);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
