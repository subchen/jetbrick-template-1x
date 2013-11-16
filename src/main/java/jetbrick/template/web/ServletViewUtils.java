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
package jetbrick.template.web;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.runtime.JetPageContext;

public class ServletViewUtils {

    public static String getOutput(JetPageContext ctx, String name) throws ServletException, IOException {
        name = ctx.getAbsolutionName(name);

        HttpServletRequest request = (HttpServletRequest) ctx.getContext().get(JetWebContext.REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx.getContext().get(JetWebContext.RESPONSE);

        StringWriter writer = new StringWriter();
        //response = new CachedHttpResponse(response, writer);
        request.getRequestDispatcher(name).forward(request, response);
        return writer.toString();
    }
}
