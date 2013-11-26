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

import java.util.ArrayList;
import java.util.List;
import jetbrick.template.utils.StringEscapeUtils;

/**
 * 模板 Class
 */
public class TemplateClassCode extends Code {
    private String packageName; // 生成的包名
    private String className; // 生成的类名
    private String templateName; // 模板名称
    private String encoding; // 模板默认输出编码
    private List<String[]> fields = new ArrayList<String[]>(32); // 全局文本字段
    private MethodCode methodCode = new MethodCode(null, "    ", false); // 方法体

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void addField(String id, String text) {
        fields.add(new String[] { id, text });
    }

    public MethodCode getMethodCode() {
        return methodCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(2048);
        if (packageName != null) {
            sb.append("package " + packageName + ";\n");
            sb.append("\n");
        }
        sb.append("import java.util.*;\n");
        sb.append("import jetbrick.template.JetContext;\n");
        sb.append("import jetbrick.template.runtime.*;\n");
        sb.append("\n");
        sb.append("@SuppressWarnings({\"all\", \"warnings\", \"unchecked\", \"unused\", \"cast\"})\n");
        sb.append("public final class " + className + " extends JetPage {\n");
        sb.append("\n");
        sb.append("  @Override\n");
        sb.append("  public void render(final JetPageContext $ctx) throws Throwable {\n");
        sb.append(methodCode.toString());
        sb.append("  }\n");
        sb.append("\n");
        sb.append("  @Override\n");
        sb.append("  public String getName() {\n");
        sb.append("    return \"" + StringEscapeUtils.escapeJava(templateName) + "\";\n");
        sb.append("  }\n");
        sb.append("\n");
        sb.append("  public static final String $ENC = \"" + encoding + "\";\n");
        for (String[] field : fields) {
            sb.append("  private static final String " + field[0] + " = \"" + StringEscapeUtils.escapeJava(field[1]) + "\";\n");
            sb.append("  private static final byte[] " + field[0] + "_bytes = JetUtils.asBytes(" + field[0] + ", $ENC);\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
