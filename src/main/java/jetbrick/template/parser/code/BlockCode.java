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
import jetbrick.template.utils.StringUtils;

/**
 * 用于给支持作用域的块节点，支持将如多行代码，每行代码自动用"\n"分割
 */
public class BlockCode implements Code {
    private final List<String> lines;
    private final String indent;

    public BlockCode(int initialCapacity, int indent) {
        this.lines = new ArrayList<String>(initialCapacity);
        this.indent = StringUtils.repeat(' ', indent * INDENT_SIZE);
    }

    public void addChild(Code code) {
        lines.add(code.getSource());
    }

    public void addChild(BlockCode code) {
        lines.addAll(code.lines);
    }

    public void addLine(String source) {
        lines.add(indent + source);
    }

    @Override
    public String getSource() {
        StringBuilder sb = new StringBuilder(512);
        if (lines != null) {
            for (String line : lines) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(line);
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getSource();
    }
}
