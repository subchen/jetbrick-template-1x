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

import javax.servlet.ServletContext;
import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import jetbrick.template.parser.VariableResolver;

class JetWebEngine extends JetEngine {
    private final ServletContext servletContext;

    protected JetWebEngine(JetConfig config, ServletContext servletContext) {
        this.servletContext = servletContext;

        load(config);

        VariableResolver resolver = getVariableResolver();
        resolver.addGlobalVariable("javax.servlet.ServletContext", JetWebContext.SERVLET_CONTEXT);
        resolver.addGlobalVariable("javax.servlet.http.HttpSession", JetWebContext.SESSION);
        resolver.addGlobalVariable("javax.servlet.http.HttpServletRequest", JetWebContext.REQUEST);
        resolver.addGlobalVariable("javax.servlet.http.HttpServletResponse", JetWebContext.RESPONSE);
        resolver.addGlobalVariable("java.util.Map<String,Object>", JetWebContext.SERVLET_CONTEXT_SCOPE);
        resolver.addGlobalVariable("java.util.Map<String,Object>", JetWebContext.SESSION_SCOPE);
        resolver.addGlobalVariable("java.util.Map<String,Object>", JetWebContext.REQUEST_SCOPE);
        resolver.addGlobalVariable("java.util.Map<String,String>", JetWebContext.PARAMETER);
        resolver.addGlobalVariable("java.util.Map<String,String[]>", JetWebContext.PARAMETER_VALUES);
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
