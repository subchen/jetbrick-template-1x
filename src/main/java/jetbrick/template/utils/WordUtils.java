package jetbrick.template.utils;

public final class WordUtils {

    public static String toUnderlineName(String s) {
        return toSpecialName(s, '_');
    }

    public static String toDashlineName(String s) {
        return toSpecialName(s, '-');
    }

    public static String toSpecialName(String s, char ch) {
        if (s == null || s.length() == 0) return s;

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(ch);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    public static String toCamelCase(String s) {
        if (s == null || s.length() == 0) return s;
        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '_' || c == '-' || c == '.') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toCapitalizeCamelCase(String s) {
        if (s == null || s.length() == 0) return s;
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
