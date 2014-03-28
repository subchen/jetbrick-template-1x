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
package jetbrick.template.web.jodd;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.*;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;
import jodd.madvoc.ActionRequest;
import jodd.madvoc.result.AbstractTemplateViewResult;

/**
 * 与 jodd madvoc 的集成。 (只支持 Jodd 3.5.1+, 3.5 之前的版本，可以用 jetx 1.2.4 版本)
 *
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public class JetTemplateResult extends AbstractTemplateViewResult {
    private String contentType;

    public JetTemplateResult() {
        super("jetx");
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    protected String locateTarget(ActionRequest actionRequest, String path) {
        if (JetWebEngineLoader.unavailable()) {
            JetWebEngineLoader.setServletContext(actionRequest.getHttpServletRequest().getSession().getServletContext());
        }

        JetEngine engine = JetWebEngineLoader.getJetEngine();
        String suffix = engine.getConfig().getTemplateSuffix();
        if (!path.endsWith(suffix)) {
            path = path + suffix;
        }
        if (engine.lookupResource(path)) {
            return path;
        }
        return null;
    }

    @Override
    protected void renderView(ActionRequest actionRequest, String path) throws Exception {
        final HttpServletRequest request = actionRequest.getHttpServletRequest();
        final HttpServletResponse response = actionRequest.getHttpServletResponse();

        JetEngine engine = JetWebEngineLoader.getJetEngine();
        response.setCharacterEncoding(engine.getConfig().getOutputEncoding());
        if (contentType != null) {
            response.setContentType(contentType);
        }

        JetContext context = new JetWebContext(request, response);
        JetTemplate template = engine.getTemplate(path);
        template.render(context, response.getOutputStream());
    }
}
