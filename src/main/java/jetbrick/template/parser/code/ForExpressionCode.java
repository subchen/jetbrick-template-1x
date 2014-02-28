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

import jetbrick.template.parser.support.TypedKlass;
import org.antlr.v4.runtime.ParserRuleContext;

/**
 * 专门用于存储 for_expression
 */
public class ForExpressionCode extends SegmentCode {
    private final String name; // 变量名

    public ForExpressionCode(TypedKlass typedKlass, String name, String source, ParserRuleContext node) {
        super(typedKlass, source, node);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
