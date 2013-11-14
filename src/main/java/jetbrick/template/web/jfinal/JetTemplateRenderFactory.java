package jetbrick.template.web.jfinal;

import jetbrick.template.web.JetWebEngineLoader;
import com.jfinal.render.IMainRenderFactory;
import com.jfinal.render.Render;

public class JetTemplateRenderFactory implements IMainRenderFactory {

    @Override
    public Render getRender(String view) {
        return new JetTemplateRender(view);
    }

    @Override
    public String getViewExtension() {
        return JetWebEngineLoader.getTemplateSuffix();
    }
}
