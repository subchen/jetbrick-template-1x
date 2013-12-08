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

import java.lang.reflect.Method;
import jetbrick.template.parser.support.ClassUtils;

public class TagCode extends Code {
    private final MethodCode methodCode;
    private final String indent;

    private String tagId;
    private Method method;
    private SegmentListCode expressionListCode;
    private int line;

    public TagCode(ScopeCode scopeCode, String indent) {
        this.methodCode = new MethodCode(scopeCode, indent + "    ", true);
        this.indent = indent;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setExpressionListCode(SegmentListCode expressionListCode) {
        this.expressionListCode = expressionListCode;
    }

    public MethodCode getMethodCode() {
        return methodCode;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(512);
        sb.append(indent).append("final JetTagContext " + tagId + " = new JetTagContext($ctx) { // line: ").append(line).append('\n');
        sb.append(indent).append("  @Override\n");
        sb.append(indent).append("  protected void render(final JetContext ").append(Code.CONTEXT_NAME).append(", final JetWriter $out) throws Throwable {\n");
        sb.append(methodCode.toString());
        sb.append(indent).append("  }\n");
        sb.append(indent).append("};\n");

        // source for invoke tag
        sb.append(indent);
        sb.append(ClassUtils.getShortClassName(method.getDeclaringClass()));
        sb.append('.').append(method.getName());
        sb.append('(');
        sb.append(tagId);
        if (expressionListCode != null && expressionListCode.size() > 0) {
            sb.append(',').append(expressionListCode.toString());
        }
        sb.append("); // line: ").append(line).append('\n');

        return sb.toString();
    }
}
