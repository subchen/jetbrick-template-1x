package jetbrick.template.web.nutz;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jetbrick.template.JetContext;
import jetbrick.template.JetTemplate;
import jetbrick.template.web.JetWebContext;
import jetbrick.template.web.JetWebEngineLoader;

import org.nutz.lang.Lang;
import org.nutz.mvc.view.AbstractPathView;

public class JetTemplateView extends AbstractPathView {
	
	public JetTemplateView(String value) {
		super(value);
	}

	public void render(HttpServletRequest req, HttpServletResponse resp, Object obj) throws Throwable {
        JetContext context = new JetWebContext(req, resp);
        JetTemplate template = JetWebEngineLoader.getJetEngine().getTemplate(evalPath(req, obj));
        try {
            template.render(context, resp.getOutputStream());
        } catch (IOException e) {
            throw Lang.wrapThrow(e);
        }
	}
}
