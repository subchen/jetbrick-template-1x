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
package jetbrick.template.web.jfinal;

import jetbrick.template.web.JetWebEngineLoader;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

public class JetTemplateRenderFactory implements IMainRenderFactory {

    @Override
    public Render getRender(String view) {
        return new JetTemplateRender(view);
    }

    @Override
    public String getViewExtension() {
        return JetWebEngineLoader.getTemplateSuffix();
    }
}
