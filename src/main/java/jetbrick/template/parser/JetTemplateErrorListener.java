package jetbrick.template.parser;

import org.antlr.v4.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetTemplateErrorListener extends BaseErrorListener {
    private static final Logger log = LoggerFactory.getLogger(JetTemplateErrorListener.class);

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        StringBuilder error = underlineError(recognizer, (Token) offendingSymbol, line, charPositionInLine, msg);
        log.error(error.toString());

        if (e != null) {
            throw e;
        }
    }

    protected StringBuilder underlineError(Recognizer<?, ?> recognizer, Token offendingToken, int line, int charPositionInLine, String msg) {
        CommonTokenStream tokens = (CommonTokenStream) recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        String[] lines = input.split("\r?\n", -1);

        StringBuilder sb = new StringBuilder(128);
        sb.append("Template parse error:");
        sb.append("\nfile: " + recognizer.getInputStream().getSourceName());
        sb.append("\nline: " + line + ", " + charPositionInLine);
        sb.append("\n msg: " + msg);
        sb.append("\n\n");
        for (int i = line - 5; i < line; i++) {
            if (i >= 0) {
                sb.append(String.format("%4d: %s%n", i + 1, lines[i]));
            }
        }
        int start = offendingToken.getStartIndex();
        int stop = offendingToken.getStopIndex();
        if (start > stop) {
            // <EOF>
            sb.append("      <EOF>\n");
            sb.append("      ^^^^^");
        } else {
            sb.append("      "); // padding
            for (int i = 0; i < charPositionInLine; i++) {
                sb.append(" ");
            }
            for (int i = start; i <= stop; i++) {
                sb.append("^");
            }
        }
        sb.append("\n");
        return sb;
    }

}
