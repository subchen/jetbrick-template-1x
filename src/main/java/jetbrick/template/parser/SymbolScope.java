package jetbrick.template.parser;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.parser.code.BlockCode;
import jetbrick.template.parser.code.LineCode;
import jetbrick.template.parser.support.TypedKlass;

/**
 * 作用域, 存储每个变量定义的Java Class。支持代码缩进 push/pop
 */
class SymbolScope {
    private final int level;
    private final SymbolScope parent;
    private final SymbolScope top;
    private final Map<String, TypedKlass> symbols;

    public SymbolScope(SymbolScope parent) {
        this.parent = parent;
        this.symbols = new HashMap<String, TypedKlass>();
        this.level = (parent == null) ? 0 : parent.level + 1;
        this.top = (parent == null) ? this : parent.top;
    }

    public SymbolScope parent() {
        return parent;
    }

    public SymbolScope top() {
        return top;
    }

    public int getLevel() {
        return level;
    }

    public TypedKlass resolve(String name) {
        TypedKlass s = symbols.get(name);
        if (s != null) return s;
        if (parent != null) {
            s = parent.resolve(name);
        }
        return s;
    }

    public boolean define(String name, TypedKlass typedKlass) {
        if (symbols.containsKey(name)) {
            return false;
        }
        symbols.put(name, typedKlass);
        return true;
    }

    public SymbolScope push() {
        return new SymbolScope(this);
    }

    public SymbolScope pop() {
        return parent;
    }

    public LineCode createLineCode(String source) {
        return new LineCode(source, level);
    }

    public BlockCode createBlockCode(int initialCapacity) {
        return new BlockCode(initialCapacity, level);
    }
}
