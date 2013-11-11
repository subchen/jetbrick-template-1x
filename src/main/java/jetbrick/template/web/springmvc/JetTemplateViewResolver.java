package jetbrick.template.web.springmvc;

import jetbrick.template.web.JetWebEngineManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

public class JetTemplateViewResolver extends AbstractTemplateViewResolver implements InitializingBean {

    public JetTemplateViewResolver() {
        setViewClass(requiredViewClass());
    }

    @Override
    protected Class<?> requiredViewClass() {
        return JetTemplateView.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JetWebEngineManager.setServletContext(getServletContext());
        if (getSuffix() == null || getSuffix().length() == 0) {
            setSuffix(JetWebEngineManager.getTemplateSuffix());
        }
    }
}
