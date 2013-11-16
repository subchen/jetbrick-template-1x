/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.parser;

import jetbrick.template.utils.StringUtils;
import org.antlr.v4.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JetTemplateErrorListener extends BaseErrorListener {
    private static final Logger log = LoggerFactory.getLogger(JetTemplateErrorListener.class);

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        CommonTokenStream tokens = (CommonTokenStream) recognizer.getInputStream();
        String input = tokens.getTokenSource().getInputStream().toString();
        String[] sourceLines = input.split("\r?\n", -1);
        Token offendingToken = (Token) offendingSymbol;

        StringBuilder sb = new StringBuilder(128);
        sb.append("Template parse failed:\n");
        sb.append(recognizer.getInputStream().getSourceName());
        sb.append(':');
        sb.append(line);
        sb.append("\nmessage: ");
        sb.append(msg);
        sb.append('\n');
        sb.append(StringUtils.getPrettyError(sourceLines, line, charPositionInLine + 1, offendingToken.getStartIndex(), offendingToken.getStopIndex(), 5));
        log.error(sb.toString());

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
