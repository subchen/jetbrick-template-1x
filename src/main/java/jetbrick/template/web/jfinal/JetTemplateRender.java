package jetbrick.template.web.jfinal;

import java.io.IOException;
import jetbrick.template.*;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineManager;
import com.jfinal.render.Render;

public class JetTemplateRender extends Render {
    private static final long serialVersionUID = 1L;
    private static JetEngine engine;

    public JetTemplateRender(String view) {
        this.view = view;
    }

    @Override
    public void render() {
        if (engine == null) {
            JetWebEngineManager.setServletContext(request.getSession().getServletContext());
            engine = JetWebEngineManager.getJetEngine();
        }

        JetContext context = new JetWebContext(request, response);
        JetTemplate template = engine.getTemplate(view);
        try {
            template.render(context, response.getOutputStream());
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
