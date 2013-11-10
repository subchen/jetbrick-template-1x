package jetbrick.template.web.struts;

import java.io.Writer;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.*;
import jetbrick.template.web.JetEngineManager;
import jetbrick.template.web.JetWebContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.template.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetTemplateEngine extends BaseTemplateEngine {
    private static final Logger log = LoggerFactory.getLogger(JetTemplateEngine.class);

    @Override
    public void renderTemplate(TemplateRenderingContext templateContext) throws Exception {
        Map<String, Object> actionContext = templateContext.getStack().getContext();
        ServletContext servletContext = (ServletContext) actionContext.get(ServletActionContext.SERVLET_CONTEXT);
        HttpServletRequest request = (HttpServletRequest) actionContext.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) actionContext.get(ServletActionContext.HTTP_RESPONSE);

        JetEngineManager.setServletContext(servletContext);
        JetEngine engine = JetEngineManager.getJetEngine();

        // get the list of templates we can use
        List<Template> templates = templateContext.getTemplate().getPossibleTemplates(this);

        JetTemplate template = null;
        String templateName = null;
        Exception exception = null;
        for (Template t : templates) {
            templateName = super.getFinalTemplateName(t);
            try {
                // try to load, and if it works, stop at the first one
                template = engine.getTemplate(templateName);
                break;
            } catch (ResourceNotFoundException e) {
                if (exception == null) {
                    exception = e;
                }
            }
        }

        if (template == null) {
            log.error("Could not load template " + templateContext.getTemplate());
            if (exception != null) {
                throw exception;
            } else {
                return;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Rendering template " + templateName);
        }

        JetContext context = new JetWebContext(servletContext, request, response);
        Writer writer = templateContext.getWriter();
        template.render(context, writer);
    }

    @Override
    protected String getSuffix() {
        return "jetx";
    }
}
