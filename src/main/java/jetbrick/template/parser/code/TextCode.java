package jetbrick.template.parser.code;

import jetbrick.template.utils.StringEscapeUtils;

/**
 * 用于存储文本内容，方便后面进行 trim 分析
 */
public class TextCode implements Code {
    private final String id;
    private String text;

    public TextCode(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public void trim(boolean timeLeft, boolean trimRight) {
        int lpos = 0;
        if (timeLeft) {
            lpos = text.indexOf('\n');
            lpos = (lpos < 0) ? 0 : lpos + 1;
        }

        int rpos = text.length();
        if (trimRight) {
            rpos = text.lastIndexOf('\n');
            rpos = (rpos < 0) ? text.length() : rpos + 1;
        }

        if (lpos < rpos) {
            text = text.substring(lpos, rpos);
        } else {
            text = null;
        }
    }

    public String getText() {
        return text;
    }

    public boolean isEmpty() {
        return text == null || text.length() == 0;
    }

    public String getTextValueFieldSource() {
        StringBuilder sb = new StringBuilder();
        sb.append("private static final String ");
        sb.append(id);
        sb.append(" = \"");
        sb.append(StringEscapeUtils.escapeJava(text));
        sb.append("\";");
        return sb.toString();
    }

    public String getTextBytesFieldSource() {
        StringBuilder sb = new StringBuilder();
        sb.append("private static final byte[] ");
        sb.append(id);
        sb.append("_bytes = JetUtils.asBytes(");
        sb.append(id);
        sb.append(", $ENC);");
        return sb.toString();
    }

    @Override
    public String getSource() {
        if (text != null) {
            return "$out.print(" + id + ", " + id + "_bytes);";
        }
        return null;
    }

    @Override
    public String toString() {
        return getSource();
    }
}
