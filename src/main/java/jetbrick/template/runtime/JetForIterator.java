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
package jetbrick.template.runtime;

import java.lang.reflect.Array;
import java.util.*;
import jetbrick.template.utils.*;

/**
 * 提供给 #for指令用的内部 Iterator 包装器
 * 
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public final class JetForIterator<T> implements Iterator<T>, JetForStatus {
    protected final Iterator<?> iterator;
    protected final int size;
    protected int index;
    protected JetForStatus status;

    public JetForIterator(Object items) {
        if (items == null) {
            iterator = EmptyIterator.INSTANCE;
            size = 0;
        } else if (items instanceof Iterator) {
            iterator = (Iterator<?>) items;
            if (items instanceof LoopIterator) {
                size = ((LoopIterator) items).getSize();
            } else {
                size = -1;
            }
        } else if (items instanceof Iterable) {
            iterator = ((Iterable<?>) items).iterator();
            if (items instanceof Collection) {
                size = ((Collection<?>) items).size();
            } else {
                size = -1;
            }
        } else if (items instanceof Map) {
            iterator = ((Map<?, ?>) items).entrySet().iterator();
            size = ((Map<?, ?>) items).size();
        } else if (items instanceof Enumeration) {
            ArrayList<?> list = Collections.list((Enumeration<?>) items);
            iterator = list.iterator();
            size = list.size();
        } else if (items.getClass().isArray()) {
            iterator = new ArrayIterator(items);
            size = Array.getLength(items);
        } else if ((items instanceof Class) && ((Class<?>) items).isEnum()) {
            List<?> itemlist = Arrays.asList(((Class<?>) items).getEnumConstants());
            iterator = itemlist.iterator();
            size = itemlist.size();
        } else {
            iterator = Collections.singleton(items).iterator();
            size = 1;
        }

        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
        Object value = iterator.next();
        index++;
        return (T) value;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * 给 for-else 用来判断是否没有元素
     */
    public boolean empty() {
        return size == 0 || (size == -1 && index == 0);
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public boolean isFirst() {
        return index == 1;
    }

    @Override
    public boolean isLast() {
        return !iterator.hasNext();
    }
}
