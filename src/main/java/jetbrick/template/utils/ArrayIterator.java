/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
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

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator implements Iterator<Object> {
    protected Object array;
    protected int startIndex = 0;
    protected int endIndex = 0;
    protected int index = 0;

    public ArrayIterator(Object array) {
        setArray(array);
    }

    public ArrayIterator(Object array, int startIndex) {
        setArray(array);
        checkBound(startIndex, "start");
        this.startIndex = startIndex;
        this.index = startIndex;
    }

    public ArrayIterator(Object array, int startIndex, int endIndex) {
        setArray(array);
        checkBound(startIndex, "start");
        checkBound(endIndex, "end");
        if (endIndex < startIndex) {
            throw new IllegalArgumentException("End index must not be less than start index.");
        }
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.index = startIndex;
    }

    protected void checkBound(int bound, String type) {
        if (bound > this.endIndex) {
            throw new ArrayIndexOutOfBoundsException("Attempt to make an ArrayIterator that " + type + "s beyond the end of the array. ");
        }
        if (bound < 0) throw new ArrayIndexOutOfBoundsException("Attempt to make an ArrayIterator that " + type + "s before the start of the array. ");
    }

    @Override
    public boolean hasNext() {
        return (this.index < this.endIndex);
    }

    @Override
    public Object next() {
        if (!(hasNext())) {
            throw new NoSuchElementException();
        }
        return Array.get(this.array, this.index++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() method is not supported.");
    }

    public Object getArray() {
        return this.array;
    }

    private void setArray(Object array) {
        this.endIndex = Array.getLength(array);
        this.startIndex = 0;
        this.array = array;
        this.index = 0;
    }

    public void reset() {
        this.index = this.startIndex;
    }
}
