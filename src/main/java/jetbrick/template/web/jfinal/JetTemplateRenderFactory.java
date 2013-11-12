package jetbrick.template.web.jfinal;

import jetbrick.template.web.JetWebEngineManager;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

public class JetTemplateRenderFactory implements IMainRenderFactory {

    public Render getRender(String view) {
        return new JetTemplateRender(view);
    }

    public String getViewExtension() {
        return JetWebEngineManager.getTemplateSuffix();
    }
}
