package jetbrick.template.web.jfinal;

import java.io.IOException;
import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;
import com.jfinal.render.Render;

public class JetTemplateRender extends Render {
    private static final long serialVersionUID = 1L;

    public JetTemplateRender(String view) {
        this.view = view;
    }

    @Override
    public void render() {
        if (!JetWebEngineLoader.available()) {
            JetWebEngineLoader.setServletContext(request.getServletContext());
        }

        JetContext context = new JetWebContext(request, response);
        JetTemplate template = JetWebEngineLoader.getJetEngine().getTemplate(view);
        try {
            template.render(context, response.getOutputStream());
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
