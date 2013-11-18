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

import jetbrick.template.utils.StringEscapeUtils;

/**
 * 用于存储文本内容，方便后面进行 trim 分析
 */
public class TextCode implements Code {
    private final String id;
    private String text;

    public TextCode(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public void trim(boolean trimLeft, boolean trimRight) {
        int lpos = 0;
        if (trimLeft) {
            int len = text.length();
            for (int i = 0; i < len; i++) {
                char c = text.charAt(i);
                if (c == ' ' || c == '\t' || c == '\r') {
                    continue;
                } else if (c == '\n') {
                    lpos = i + 1;
                    break;
                } else {
                    break;
                }
            }
        }

        int rpos = text.length();
        if (trimRight) {
            for (int i = text.length() - 1; i >= 0; i--) {
                char c = text.charAt(i);
                if (c == ' ' || c == '\t' || c == '\r') {
                    continue;
                } else if (c == '\n') {
                    rpos = i + 1;
                    break;
                } else {
                    break;
                }
            }
        }

        if (lpos < rpos) {
            text = text.substring(lpos, rpos);
        } else {
            text = null;
        }
    }

    public String getText() {
        return text;
    }

    public boolean isEmpty() {
        return text == null || text.length() == 0;
    }

    public String getTextValueFieldSource() {
        StringBuilder sb = new StringBuilder();
        sb.append("private static final String ");
        sb.append(id);
        sb.append(" = \"");
        sb.append(StringEscapeUtils.escapeJava(text));
        sb.append("\";");
        return sb.toString();
    }

    public String getTextBytesFieldSource() {
        StringBuilder sb = new StringBuilder();
        sb.append("private static final byte[] ");
        sb.append(id);
        sb.append("_bytes = JetUtils.asBytes(");
        sb.append(id);
        sb.append(", $ENC);");
        return sb.toString();
    }

    @Override
    public String getSource() {
        if (text != null) {
            return "$out.print(" + id + ", " + id + "_bytes);";
        }
        return null;
    }

    @Override
    public String toString() {
        return getSource();
    }
}
