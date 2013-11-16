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
package jetbrick.template.web.springmvc;

import jetbrick.template.web.JetWebEngineLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

public class JetTemplateViewResolver extends AbstractTemplateViewResolver implements InitializingBean {

    public JetTemplateViewResolver() {
        setViewClass(requiredViewClass());
    }

    @Override
    protected Class<?> requiredViewClass() {
        return JetTemplateView.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JetWebEngineLoader.setServletContext(getServletContext());
        if (getSuffix() == null || getSuffix().length() == 0) {
            setSuffix(JetWebEngineLoader.getTemplateSuffix());
        }
    }
}
