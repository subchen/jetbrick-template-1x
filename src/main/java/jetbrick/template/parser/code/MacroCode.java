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

public class MacroCode extends Code {
    private final MethodCode methodCode;
    private String name;
    private SegmentListCode defineListCode;
    private int line;

    public MacroCode(ScopeCode scopeCode) {
        this.methodCode = new MethodCode(scopeCode, "    ", true);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefineListCode(SegmentListCode defineListCode) {
        this.defineListCode = defineListCode;
    }

    public SegmentListCode getDefineListCode() {
        return defineListCode;
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
        sb.append("  // line: ").append(line).append('\n');
        sb.append("  protected static void $macro_");
        sb.append(name);
        sb.append("(final JetContext ").append(Code.CONTEXT_NAME).append(", final JetWriter $out");
        if (defineListCode != null && defineListCode.size() > 0) {
            sb.append(',').append(defineListCode.toString());
        }
        sb.append(") throws Throwable { // line: ");
        sb.append(line).append('\n');
        sb.append(methodCode.toString());
        sb.append("  }\n");
        return sb.toString();
    }
}
