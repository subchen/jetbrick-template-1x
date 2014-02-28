/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.parser.code;

import java.util.HashMap;
import java.util.Map;
import jetbrick.template.parser.support.TypedKlass;

/**
 * 带作用域的 Code
 */
public class ScopeCode extends Code {
    protected final ScopeCode parent; // 父作用域
    protected final String indent;
    protected final Map<String, TypedKlass> symbols; // 定义的符号表
    protected Code bodyCode;

    public ScopeCode(ScopeCode parent, String indent) {
        this.parent = parent;
        this.indent = indent;
        this.symbols = new HashMap<String, TypedKlass>();
    }

    // 查找已经定义的变量类型
    public TypedKlass resolve(String name, boolean deep) {
        TypedKlass klass = symbols.get(name);
        if (klass != null) {
            return klass;
        }
        if (!deep && this instanceof MethodCode) return null;

        if (parent != null) {
            klass = parent.resolve(name, deep);
        }
        return klass;
    }

    // 尝试在当前作用域下定义一个变量，如果已经定义，则返回 false
    public boolean define(String name, TypedKlass typedKlass) {
        return define(name, typedKlass, false);
    }

    // 尝试在当前作用域下定义一个变量，如果已经定义，则返回 false
    public boolean define(String name, TypedKlass typedKlass, boolean isGlobalContext) {
        if (resolve(name, false) != null) {
            return false;
        }

        symbols.put(name, typedKlass);

        if (isGlobalContext) {
            // 注意：不光要当前的作用域定义，也要在Global作用域定义，防止后面重定义
            lookupMethodCode().addContext(name, typedKlass);
        }
        return true;
    }

    /**
     * 向上查找 MethodCode
     */
    private MethodCode lookupMethodCode() {
        ScopeCode c = this;
        while (c != null) {
            if (c instanceof MethodCode) {
                return (MethodCode) c;
            }
            c = c.parent;
        }
        return null;
    }

    public ScopeCode push() {
        return new ScopeCode(this, indent + "  ");
    }

    public ScopeCode pop() {
        return parent;
    }

    public BlockCode createBlockCode(int initialCapacity) {
        return new BlockCode(initialCapacity, indent);
    }

    public LineCode createLineCode(String source) {
        return new LineCode(indent + source + "\n");
    }

    public TagCode createTagCode() {
        return new TagCode(this, indent);
    }

    public MacroCode createMacroCode() {
        return new MacroCode(this);
    }

    public void setBodyCode(Code code) {
        this.bodyCode = code;
    }

    @Override
    public String toString() {
        return bodyCode.toString();
    }
}
