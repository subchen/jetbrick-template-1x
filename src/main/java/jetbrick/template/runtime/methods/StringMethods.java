package jetbrick.template.runtime.methods;

public final class StringMethods {

    public static String repeat(String value, int count) {
        if (value == null || value.length() == 0) return value;
        if (count <= 0) return value;

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < count; i++) {
            buf.append(value);
        }
        return buf.toString();
    }
}
