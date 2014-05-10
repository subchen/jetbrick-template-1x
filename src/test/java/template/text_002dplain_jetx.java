package template;

import jetbrick.template.JetContext;
import jetbrick.template.runtime.*;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public final class text_002dplain_jetx extends JetPage {

    @Override
    public void render(final JetPageContext $ctx) throws Throwable {
        final JetContext context = $ctx.getContext();
        final JetWriter $out = $ctx.getWriter();
        $out.print($txt_1, $txt_1_bytes);
        $out.flush();
    }

    @Override
    public String getName() {
        return "/template/text-plain.jetx";
    }

    public static final String $ENC = "utf-8";
    private static final String $txt_1 = "1234567890";
    private static final byte[] $txt_1_bytes = JetUtils.asBytes($txt_1, $ENC);
}
