package jetbrick.template.web;

import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import jetbrick.template.parser.VariableResolver;

class JetWebEngine extends JetEngine {

    protected JetWebEngine(JetConfig config) {
        super(config);

        VariableResolver resolver = getVariableResolver();
        resolver.addGlobalVariable("javax.servlet.ServletContext", JetWebContext.SERVLET_CONTEXT);
        resolver.addGlobalVariable("javax.servlet.http.HttpSession", JetWebContext.SESSION);
        resolver.addGlobalVariable("javax.servlet.http.HttpServletRequest", JetWebContext.REQUEST);
        resolver.addGlobalVariable("javax.servlet.http.HttpServletResponse", JetWebContext.RESPONSE);
        resolver.addGlobalVariable("java.util.Map<String,String[]>", JetWebContext.PARAMETERS);
        resolver.addGlobalVariable("javax.servlet.http.Cookie[]", JetWebContext.COOKIES);
    }
}
