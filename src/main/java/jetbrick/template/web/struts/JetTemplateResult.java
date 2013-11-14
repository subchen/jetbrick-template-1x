package jetbrick.template.web.struts;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import com.opensymphony.xwork2.ActionInvocation;

public class JetTemplateResult extends StrutsResultSupport {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doExecute(String location, ActionInvocation ai) throws Exception {
        Map<String, Object> model = ai.getStack().getContext();
        HttpServletRequest request = (HttpServletRequest) model.get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) model.get(ServletActionContext.HTTP_RESPONSE);

        if (JetWebEngineLoader.unavailable()) {
            ServletContext servletContext = (ServletContext) model.get(ServletActionContext.SERVLET_CONTEXT);
            JetWebEngineLoader.setServletContext(servletContext);
        }

        JetContext context = new JetWebContext(request, response, model);
        JetTemplate template = JetWebEngineLoader.getJetEngine().getTemplate(location);
        template.render(context, response.getOutputStream());
    }
}
