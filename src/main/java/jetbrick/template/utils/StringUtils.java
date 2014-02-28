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
package jetbrick.template.utils;

import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return cs != null && cs.length() > 0;
    }

    public static String repeat(char ch, int count) {
        char[] buf = new char[count];
        for (int i = count - 1; i >= 0; --i) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String repeat(String value, int count) {
        if (value == null || value.length() == 0) return value;
        if (count <= 0) return value;

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < count; i++) {
            buf.append(value);
        }
        return buf.toString();
    }

    public static String deleteWhitespace(String str) {
        if (str == null || str.length() == 0) return str;
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) return str;
        return new String(chs, 0, count);
    }

    public static String trimToEmpty(String str) {
        if (str == null) {
            return "";
        }
        return str.trim();
    }

    public static String[] split(String str, char delimiter) {
        List<String> results = new ArrayList<String>();

        int ipos = 0, lastpos = 0;
        while ((ipos = str.indexOf(delimiter, lastpos)) != -1) {
            results.add(str.substring(lastpos, ipos));
            lastpos = ipos + 1;
        }
        if (lastpos < str.length()) {
            results.add(str.substring(lastpos));
        }
        return results.toArray(new String[results.size()]);
    }

    public static String asJavaBytes(String s, String encoding) {
        if (s == null || s.length() == 0) return "{}";
        try {
            return asJavaBytes(s.getBytes(encoding));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String asJavaBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "{}";

        int length = bytes.length;
        StringBuilder sb = new StringBuilder(length * 4);
        sb.append('{');
        sb.append(bytes[0]);
        for (int i = 1; i < length; i++) {
            sb.append(',').append(bytes[i]);
        }
        sb.append('}');
        return sb.toString();
    }

    public static CharSequence getPrettyError(String[] sourceLines, int line, int column, int start, int stop, int show_lines) {
        StringBuilder sb = new StringBuilder(128);
        for (int i = line - show_lines; i < line; i++) {
            if (i >= 0) {
                String sourceLine = sourceLines[i];

                // 1 个 Tab 变成 4 个空格
                if (i == line - 1) {
                    int origin_column = Math.min(column, sourceLine.length() - 1);
                    for (int j = 0; j < origin_column; j++) {
                        char c = sourceLine.charAt(j);
                        if (c == '\t') {
                            column += 3;
                        } else if (c >= '\u2E80' && c <= '\uFE4F') {
                            column++; // 中日韩统一表意文字（CJK Unified Ideographs）
                        }
                    }
                }
                sourceLine = sourceLine.replaceAll("\\t", "    ");
                sb.append(String.format("%4d: %s%n", i + 1, sourceLine));
            }
        }
        if (start > stop) {
            // <EOF>
            sb.append("      <EOF>\n");
            sb.append("      ^^^^^");
        } else {
            sb.append("      "); // padding
            for (int i = 0; i < column - 1; i++) {
                sb.append(' ');
            }
            for (int i = start; i <= stop; i++) {
                sb.append('^');
            }
        }
        sb.append('\n');
        return sb;
    }

}
