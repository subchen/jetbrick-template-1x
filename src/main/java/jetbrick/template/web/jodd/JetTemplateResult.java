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
package jetbrick.template.web.jodd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.*;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.result.ActionResult;

/**
 * 与 jodd madvoc 的集成。
 * 
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public class JetTemplateResult extends ActionResult {
    private String contentType;

    public JetTemplateResult() {
        super("jetx");
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void render(ActionRequest actionRequest, Object resultObject, String resultValue, String resultPath) throws Exception {
        final HttpServletRequest request = actionRequest.getHttpServletRequest();
        final HttpServletResponse response = actionRequest.getHttpServletResponse();

        if (JetWebEngineLoader.unavailable()) {
            JetWebEngineLoader.setServletContext(request.getSession().getServletContext());
        }
        JetEngine engine = JetWebEngineLoader.getJetEngine();

        response.setCharacterEncoding(engine.getConfig().getOutputEncoding());
        if (contentType != null) {
            response.setContentType(contentType);
        }

        JetContext context = new JetWebContext(request, response);
        JetTemplate template = engine.getTemplate(resultPath);
        template.render(context, response.getOutputStream());
    }
}
