package jetbrick.template.parser;

import org.antlr.v4.runtime.*;

public class JetTemplateErrorStrategy extends DefaultErrorStrategy {
    @Override
    public void recover(Parser recognizer, RecognitionException e) {
        throw e;
    }

    @Override
    public Token recoverInline(Parser recognizer) throws RecognitionException {
        reportMissingToken(recognizer);
        return null;
    }

    @Override
    public void sync(Parser recognizer) {
    }
}
