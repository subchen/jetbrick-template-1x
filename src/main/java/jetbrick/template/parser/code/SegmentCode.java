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
package jetbrick.template.parser.code;

import jetbrick.template.parser.support.TypedKlass;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * 用于存储一个表达式节点
 */
public class SegmentCode extends Code {
    private final TypedKlass typedKlass; // 支持泛型描述的类型
    private final String source; // 翻译成的源代码
    private final ParserRuleContext node; // 对应的 ParseTreeNode

    public SegmentCode(Class<?> klass, String source, ParserRuleContext node) {
        this(TypedKlass.create(klass), source, node);
    }

    public SegmentCode(Class<?> klass, TypedKlass[] typeArgs, String source, ParserRuleContext node) {
        this(TypedKlass.create(klass, typeArgs), source, node);
    }

    public SegmentCode(TypedKlass typedKlass, String source, ParserRuleContext node) {
        this.typedKlass = typedKlass;
        this.source = source;
        this.node = node;
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

    public SegmentCode asBoxedSegmentCode() {
        Class<?> klass = getKlass();
        String newSource = null;
        if (klass == null) {
            return this;
        } else if (Byte.TYPE.equals(klass)) {
            newSource = "Byte.valueOf(" + source + ")";
        } else if (Short.TYPE.equals(klass)) {
            newSource = "Short.valueOf(" + source + ")";
        } else if (Integer.TYPE.equals(klass)) {
            newSource = "Integer.valueOf(" + source + ")";
        } else if (Long.TYPE.equals(klass)) {
            newSource = "Long.valueOf(" + source + ")";
        } else if (Float.TYPE.equals(klass)) {
            newSource = "Float.valueOf(" + source + ")";
        } else if (Double.TYPE.equals(klass)) {
            newSource = "Double.valueOf(" + source + ")";
        } else if (Boolean.TYPE.equals(klass)) {
            newSource = "Boolean.valueOf(" + source + ")";
        } else if (Character.TYPE.equals(klass)) {
            newSource = "Character.valueOf(" + source + ")";
        }

        if (newSource == null) {
            return this;
        } else {
            return new SegmentCode(typedKlass.asBoxedTypedKlass(), newSource, node);
        }
    }

    public SegmentCode asUnboxedSegmentCode() {
        Class<?> klass = getKlass();
        String newSource = null;
        if (klass == null) {
            return this;
        } else if (Byte.class.equals(klass)) {
            newSource = "(" + source + "==null ? (byte) 0 : " + source + ".byteValue())";
        } else if (Short.class.equals(klass)) {
            newSource = "(" + source + "==null ? (short) 0 : " + source + ".shortValue())";
        } else if (Integer.class.equals(klass)) {
            newSource = "(" + source + "==null ? 0 : " + source + ".intValue())";
        } else if (Long.class.equals(klass)) {
            newSource = "(" + source + "==null ? 0L : " + source + ".longValue())";
        } else if (Float.class.equals(klass)) {
            newSource = "(" + source + "==null ? 0.0F : " + source + ".floatValue())";
        } else if (Double.class.equals(klass)) {
            newSource = "(" + source + "==null ? 0.0D : " + source + ".doubleValue())";
        } else if (Boolean.class.equals(klass)) {
            newSource = "(" + source + "==null ? false : " + source + ".booleanValue())";
        } else if (Character.class.equals(klass)) {
            newSource = "(" + source + "==null ? '\0' : " + source + ".charValue())";
        }

        if (newSource == null) {
            return this;
        } else {
            return new SegmentCode(typedKlass.asUnboxedTypedKlass(), newSource, node);
        }
    }
    
    public ParserRuleContext getNode() {
        return node;
    }

    @Override
    public String toString() {
        return source;
    }
}
