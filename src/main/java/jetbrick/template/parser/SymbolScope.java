/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
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
