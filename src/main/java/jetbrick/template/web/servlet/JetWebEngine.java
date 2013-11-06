package jetbrick.template.web.servlet;

import java.util.Properties;
import jetbrick.template.JetEngine;

public class JetWebEngine extends JetEngine {

    public JetWebEngine(Properties config) {
        super(config);

        resolver.addGlobalVariable("javax.servlet.ServletContext", "application");
        resolver.addGlobalVariable("javax.servlet.http.HttpSession", "session");
        resolver.addGlobalVariable("javax.servlet.http.HttpServletRequest", "request");
        resolver.addGlobalVariable("javax.servlet.http.HttpServletResponse", "response");
        resolver.addGlobalVariable("java.util.Map<String,String[]>", "parameters");
        resolver.addGlobalVariable("javax.servlet.http.Cookie[]", "cookies");
    }
}
