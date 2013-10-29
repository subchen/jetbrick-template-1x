package jetbrick.template.runtime.methods;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import jetbrick.template.utils.StringEscapeUtils;

public final class StringEscapeMethods {

    public static String escapeJava(String value) {
        return StringEscapeUtils.escapeJava(value);
    }

    public static String unescapeJava(String value) {
        return StringEscapeUtils.unescapeJava(value);
    }

    public static String escapeJavaScript(String value) {
        return StringEscapeUtils.escapeJavaScript(value);
    }

    public static String unescapeJavaScript(String value) {
        return StringEscapeUtils.unescapeJavaScript(value);
    }

    public static String escapeXml(String value) {
        return StringEscapeUtils.escapeXml(value);
    }

    public static String unescapeXml(String value) {
        return StringEscapeUtils.unescapeXml(value);
    }

    public static String escapeUrl(String value) {
        return escapeUrl(value, "UTF-8");
    }

    public static String escapeUrl(String value, String encoding) {
        try {
            return value == null ? null : URLEncoder.encode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    public static String unescapeUrl(String value) {
        return unescapeUrl(value, "UTF-8");
    }

    public static String unescapeUrl(String value, String encoding) {
        try {
            return value == null ? null : URLDecoder.decode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}
