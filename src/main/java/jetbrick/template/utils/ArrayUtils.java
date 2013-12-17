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

import java.util.Iterator;

public final class ArrayUtils {
    public static final int[] EMPTY_INTEGER_ARRAY = new int[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    public static int[] range(int start, int stop, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be zero.");
        }
        int length = (stop - start + step) / step;
        if (length <= 0) return ArrayUtils.EMPTY_INTEGER_ARRAY;

        int[] results = new int[length];
        if (step > 0) {
            for (int i = 0, n = start; n <= stop; n += step) {
                results[i++] = n;
            }
        } else {
            for (int i = 0, n = start; n >= stop; n += step) {
                results[i++] = n;
            }
        }
        return results;
    }

    /**
     * @since 1.0.2
     */
    public static Iterator<Integer> iterator(final int start, final int stop, final int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be zero.");
        }
        return new LoopIterator(start, stop, step);
    }
}
