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

/**
 * 主要用于存储 expression_list 和 type_list
 */
public class SegmentListCode implements Code {
    private final List<SegmentCode> children;

    public SegmentListCode(int initialCapacity) {
        children = new ArrayList<SegmentCode>(initialCapacity);
    }

    public void addChild(SegmentCode code) {
        children.add(code);
    }

    public List<SegmentCode> getChildren() {
        return children;
    }

    public SegmentCode getChild(int i) {
        return children.get(i);
    }

    public int size() {
        return children.size();
    }

    @Override
    public String getSource() {
        StringBuilder sb = new StringBuilder(32);
        for (SegmentCode code : children) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(code.getSource());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getSource();
    }
}
