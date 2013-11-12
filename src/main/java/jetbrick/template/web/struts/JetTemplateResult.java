package jetbrick.template.web.struts;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.*;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineManager;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import com.opensymphony.xwork2.ActionInvocation;

public class JetTemplateResult extends StrutsResultSupport {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doExecute(String location, ActionInvocation ai) throws Exception {
        Map<String, Object> model = ai.getStack().getContext();
        ServletContext servletContext = (ServletContext) model.get(ServletActionContext.SERVLET_CONTEXT);
        HttpServletRequest request = (HttpServletRequest) model.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) model.get(ServletActionContext.HTTP_RESPONSE);

        JetWebEngineManager.setServletContext(servletContext);
        JetEngine engine = JetWebEngineManager.getJetEngine();

        JetContext context = new JetWebContext(request, response, model);
        JetTemplate template = engine.getTemplate(location);
        template.render(context, response.getOutputStream());
    }
}
