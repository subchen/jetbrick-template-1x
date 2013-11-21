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
package jetbrick.template.runtime;

import jetbrick.template.JetTemplate;

/**
 * 系统自带的 Tag
 */
public final class JetTags {

    /**
     * 实现 layout 的 tag.
     * 
     * @param ctx Tag 上下文对象
     * @param name layout 模板路径
     */
    public static void layout(JetTagContext ctx, String name) {
        String bodyContext = ctx.getBodyContent();
        ctx.getContext().put("bodyContext", bodyContext);

        JetTemplate template = ctx.getEngine().getTemplate(name);
        template.render(ctx.getContext(), ctx.getWriter());
    }
}
