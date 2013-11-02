package jetbrick.template.utils;

import java.text.*;
import java.util.Date;

public final class DateUtils {
    private static final FastDateFormat DEFAULT_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    //@formatter:off
    private static final String[] DATE_PATTERNS = new String[] { 
        "yyyy-MM-dd HH:mm:ss.SSS", 
        "yyyy-MM-dd HH:mm:ss", 
        "yyyy-MM-dd", 
        "HH:mm:ss"
    };
    //@formatter:on

    public static Date asDate(String value) {
        value = (value != null) ? value.trim() : null;
        ParsePosition pp = null;
        Date d = null;
        for (int i = 0; d == null && i < DATE_PATTERNS.length; i++) {
            DateFormat df = new SimpleDateFormat(DATE_PATTERNS[i]);
            df.setLenient(false);
            try {
                pp = new ParsePosition(0);
                d = df.parse(value, pp);
                if (pp.getIndex() != value.length()) {
                    d = null;
                }
            } catch (Exception e) {
                // try next pattern
            }
        }
        return d;
    }

    public static Date asDate(String value, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setLenient(false);
        try {
            ParsePosition pp = new ParsePosition(0);
            Date d = df.parse(value, pp);
            if (pp.getIndex() != value.length()) {
                d = null;
            }
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public static String format(Date value) {
        return DEFAULT_DATE_FORMAT.format(value);
    }

    public static String format(Date value, String format) {
        return FastDateFormat.getInstance(format).format(value);
    }
}
