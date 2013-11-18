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
package jetbrick.template.web.jfinal;

import java.io.IOException;
import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;
import com.jfinal.render.Render;

public class JetTemplateRender extends Render {
    private static final long serialVersionUID = 1L;

    public JetTemplateRender(String view) {
        this.view = view;
    }

    @Override
    public void render() {
        if (JetWebEngineLoader.unavailable()) {
            JetWebEngineLoader.setServletContext(request.getSession().getServletContext());
        }

        JetContext context = new JetWebContext(request, response);
        JetTemplate template = JetWebEngineLoader.getJetEngine().getTemplate(view);
        try {
            template.render(context, response.getOutputStream());
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
