package jetbrick.template.web.springmvc;

import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebEngineManager;
import org.springframework.web.servlet.view.AbstractTemplateView;

public class JetTemplateView extends AbstractTemplateView {

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JetContext context = JetWebEngineManager.createJetContext(request, response, model);
        JetTemplate template = JetWebEngineManager.getJetEngine().getTemplate(getUrl());
        template.render(context, response.getOutputStream());
    }

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        return JetWebEngineManager.getJetEngine().lookupResource(getUrl());
    }
}
