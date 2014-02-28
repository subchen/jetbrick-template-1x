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

import java.util.LinkedHashMap;
import java.util.Map;
import jetbrick.template.parser.support.TypedKlass;

/**
 * render() 方法体 
 */
public class MethodCode extends ScopeCode {
    // 方法内全局 Context
    private final Map<String, TypedKlass> contextMap = new LinkedHashMap<String, TypedKlass>(8);
    private final boolean isEmbedClass; // 是否嵌入在 #Tag 的方法体里面(内部匿名类) 

    public MethodCode(ScopeCode parent, String indent, boolean isEmbedClass) {
        super(parent, indent);
        this.isEmbedClass = isEmbedClass;
    }

    public void addContext(String name, TypedKlass klass) {
        contextMap.put(name, klass);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(1024);
        if (!isEmbedClass) {
            sb.append(indent).append("final JetContext context = $ctx.getContext();\n");
            sb.append(indent).append("final JetWriter $out = $ctx.getWriter();\n");
        }
        for (Map.Entry<String, TypedKlass> entry : contextMap.entrySet()) {
            String type = entry.getValue().asBoxedTypedKlass().getSource();
            String name = entry.getKey();
            sb.append(indent).append(type).append(' ').append(name);
            sb.append(" = (").append(type).append(") context.get(\"").append(name).append("\");\n");
        }
        sb.append(super.toString());
        sb.append(indent).append("$out.flush();\n");
        return sb.toString();
    }
}
