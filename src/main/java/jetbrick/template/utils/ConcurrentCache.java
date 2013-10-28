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

    public void remove(K key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public abstract V doGetValue(K key);
}
