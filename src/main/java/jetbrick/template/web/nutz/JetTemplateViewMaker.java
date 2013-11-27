package jetbrick.template.web.nutz;

import jetbrick.template.web.JetWebEngineLoader;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

/**
 * nutz与JetTemplate集成
 * @author wendal(wendal1985@gmail.com)
 */
public class JetTemplateViewMaker implements ViewMaker {

	public JetTemplateViewMaker() {
		JetWebEngineLoader.setServletContext(Mvcs.getServletContext());
	}
	
	public View make(Ioc ioc, String type, final String value) {
		if (JetWebEngineLoader.getTemplateSuffix().equals(type)) {
			return new JetTemplateView(value);
		}
		return null;
	}
}

