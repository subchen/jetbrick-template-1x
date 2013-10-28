package jetbrick.template.parser.code;

import jetbrick.template.parser.support.TypedKlass;

/**
 * 用于存储一个表达式节点
 */
public class SegmentCode implements Code {
    private final TypedKlass typedKlass; // 支持泛型描述的类型
    private final String source; // 翻译成的源代码

    public SegmentCode(Class<?> klass, String source) {
        this(klass, TypedKlass.EMPTY_TYPE_ARGS, source);
    }

    public SegmentCode(Class<?> klass, TypedKlass[] typeArgs, String source) {
        this(TypedKlass.create(klass, typeArgs), source);
    }

    public SegmentCode(TypedKlass typedKlass, String source) {
        this.typedKlass = typedKlass;
        this.source = source;
    }

    public TypedKlass getTypedKlass() {
        return typedKlass;
    }

    public Class<?> getKlass() {
        return typedKlass == null ? null : typedKlass.getKlass();
    }

    public TypedKlass[] getTypeArgs() {
        return typedKlass == null ? TypedKlass.EMPTY_TYPE_ARGS : typedKlass.getTypeArgs();
    }

    public String getKlassName() {
        return typedKlass == null ? null : typedKlass.getSource();
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
