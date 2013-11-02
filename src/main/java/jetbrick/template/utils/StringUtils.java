package jetbrick.template.utils;

public final class StringUtils {

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return cs != null && cs.length() > 0;
    }

    public static String repeat(char ch, int count) {
        char[] buf = new char[count];
        for (int i = count - 1; i >= 0; --i) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String repeat(String value, int count) {
        if (value == null || value.length() == 0) return value;
        if (count <= 0) return value;

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < count; i++) {
            buf.append(value);
        }
        return buf.toString();
    }
    
    public static String deleteWhitespace(String str) {
        if (str == null || str.length() == 0) return str;
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) return str;
        return new String(chs, 0, count);
    }

    public static String asJavaBytes(String s, String encoding) {
        if (s == null || s.length() == 0) return "{}";
        try {
            return asJavaBytes(s.getBytes(encoding));
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String asJavaBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "{}";

        int length = bytes.length;
        StringBuilder sb = new StringBuilder(length * 4);
        sb.append('{');
        sb.append(bytes[0]);
        for (int i = 1; i < length; i++) {
            sb.append(',').append(bytes[i]);
        }
        sb.append('}');
        return sb.toString();
    }

    public static CharSequence getPrettyError(String[] sourceLines, int line, int column, int start, int stop, int show_lines) {
        StringBuilder sb = new StringBuilder(128);
        for (int i = line - show_lines; i < line; i++) {
            if (i >= 0) {
                sb.append(String.format("%4d: %s%n", i + 1, sourceLines[i]));
            }
        }
        if (start > stop) {
            // <EOF>
            sb.append("      <EOF>\n");
            sb.append("      ^^^^^");
        } else {
            sb.append("      "); // padding
            for (int i = 0; i < column - 1; i++) {
                sb.append(' ');
            }
            for (int i = start; i <= stop; i++) {
                sb.append('^');
            }
        }
        sb.append('\n');
        return sb;
    }

}
