package jetbrick.template.web;

import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import jetbrick.template.parser.VariableResolver;

class JetWebEngine extends JetEngine {

    protected JetWebEngine(JetConfig config) {
        super(config);

        VariableResolver resolver = getVariableResolver();
        resolver.addGlobalVariable("javax.servlet.ServletContext", "application");
        resolver.addGlobalVariable("javax.servlet.http.HttpSession", "session");
        resolver.addGlobalVariable("javax.servlet.http.HttpServletRequest", "request");
        resolver.addGlobalVariable("javax.servlet.http.HttpServletResponse", "response");
        resolver.addGlobalVariable("java.util.Map<String,String[]>", "parameters");
        resolver.addGlobalVariable("javax.servlet.http.Cookie[]", "cookies");
    }
}
