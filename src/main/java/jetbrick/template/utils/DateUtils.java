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
package jetbrick.template.utils;

import java.text.*;
import java.util.Date;

public final class DateUtils {
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
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
    }

    public static String format(Date value, String format) {
        return new SimpleDateFormat(format).format(value);
    }
}
