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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ConcurrentCache<K, V> {
    private final ConcurrentMap<K, VolatileReference<V>> cache = new ConcurrentHashMap<K, VolatileReference<V>>();

    public V get(K key) {
        VolatileReference<V> ref = cache.get(key);
        if (ref == null) {
            ref = new VolatileReference<V>(); // quickly
            VolatileReference<V> old = cache.putIfAbsent(key, ref);
            if (old != null) { // duplicate
                ref = old;
            }
        }
        assert (ref != null);

        V value = ref.get();
        if (value == null) {
            synchronized (ref) { // reference lock
                value = ref.get();
                if (value == null) { // double check
                    value = doGetValue(key); // slowly
                    ref.set(value);
                }
            }
        }

        return value;
    }

    protected abstract V doGetValue(K key);

    public void remove(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    static class VolatileReference<T> {
        private volatile T value;

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
        }
    }
}
