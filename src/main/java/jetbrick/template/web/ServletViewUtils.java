package jetbrick.template.web;

import java.io.IOException;
import java.io.StringWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jetbrick.template.runtime.JetRuntimeContext;

public class ServletViewUtils {

    public static String getOutput(JetRuntimeContext ctx, String name) throws ServletException, IOException {
        name = ctx.getAbsolutionName(name);

        HttpServletRequest request = (HttpServletRequest) ctx.getContext().get(JetWebContext.REQUEST);
        HttpServletResponse response = (HttpServletResponse) ctx.getContext().get(JetWebContext.RESPONSE);

        StringWriter writer = new StringWriter();
        //response = new CachedHttpResponse(response, writer);
        request.getRequestDispatcher(name).forward(request, response);
        return writer.toString();
    }
}
