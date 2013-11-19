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
package jetbrick.template.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import jetbrick.template.*;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;

/**
 * 直接作为 Servlet 使用。需要在 web.xml 中作如下配置。
 * <pre><xmp>
 * <servlet>
 *      <servlet-name>jetbrick-template</servlet-name>
 *      <servlet-class>jetbrick.template.web.servlet.JetTemplateServlet</servlet-class>
 *      <load-on-startup>1</load-on-startup>
 *  </servlet>
 *  <servlet-mapping>
 *      <servlet-name>jetbrick-template</servlet-name>
 *      <url-pattern>*.jetx</url-pattern>
 *  </servlet-mapping>
 *  </xmp></pre>
 */
public class JetTemplateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        JetWebEngineLoader.setServletContext(getServletContext());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JetContext context = new JetWebContext(request, response);

        String path = request.getPathInfo();
        if (path == null || path.length() == 0) {
            path = request.getServletPath();
        }
        if (path.endsWith("/")) {
            path = path + "/index.jetx";
        }

        try {
            JetEngine engine = JetWebEngineLoader.getJetEngine();
            response.setCharacterEncoding(engine.getConfig().getOutputEncoding());
            
            JetTemplate template = engine.getTemplate(path);
            template.render(context, response.getOutputStream());
        } catch (ResourceNotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Template not found: " + path);
        }
    }
}
