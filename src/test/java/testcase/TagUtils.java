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
package testcase;

import java.io.IOException;
import jetbrick.template.runtime.JetTagContext;

public class TagUtils {

    public static void testTag(JetTagContext ctx) throws IOException {
        String body = ctx.getBodyContent();
        ctx.getWriter().print(body.toUpperCase());
    }

    public static void testTag(JetTagContext ctx, String name) throws IOException {
        ctx.writeBodyContent();
        ctx.getWriter().print(name);
    }
}
