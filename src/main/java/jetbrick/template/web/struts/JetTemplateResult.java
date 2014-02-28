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
package jetbrick.template.web.struts;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import com.opensymphony.xwork2.ActionInvocation;

public class JetTemplateResult extends StrutsResultSupport {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doExecute(String location, ActionInvocation ai) throws Exception {
        Map<String, Object> model = ai.getStack().getContext();
        HttpServletRequest request = (HttpServletRequest) model.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) model.get(ServletActionContext.HTTP_RESPONSE);

        if (JetWebEngineLoader.unavailable()) {
            ServletContext servletContext = (ServletContext) model.get(ServletActionContext.SERVLET_CONTEXT);
            JetWebEngineLoader.setServletContext(servletContext);
        }

        JetContext context = new JetWebContext(request, response, null);
        context.put("action", ai.getAction());
        context.put("valueStack", model);
        JetTemplate template = JetWebEngineLoader.getJetEngine().getTemplate(location);
        template.render(context, response.getOutputStream());
    }
}
