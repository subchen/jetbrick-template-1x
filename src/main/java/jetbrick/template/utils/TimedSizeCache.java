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

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简单具有超时检测的 cache 实现.
 *
 * @since 1.20.
 * @author Guoqiang Chen
 */
public class TimedSizeCache {
    public static final String CACHE_KEY = TimedSizeCache.class.getName();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    protected Map<String, CacheEntry> cacheMap;
    protected int maxSize; // max cache size, 0 = no limit

    public TimedSizeCache(int maxSize) {
        this.maxSize = maxSize;
        this.cacheMap = new HashMap<String, CacheEntry>();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void put(String key, Object object) {
        put(key, object, 0);
    }

    public void put(String key, Object object, long timeout) {
        writeLock.lock();
        try {
            CacheEntry entry = new CacheEntry(key, object, timeout);
            if (isFull()) {
                pruneCache();
            }
            cacheMap.put(key, entry);
        } finally {
            writeLock.unlock();
        }
    }

    public Object get(String key) {
        readLock.lock();
        try {
            CacheEntry entry = cacheMap.get(key);
            if (entry == null) {
                return null;
            }
            if (entry.isExpired()) {
                cacheMap.remove(key);
                return null;
            }
            return entry.getObject();
        } finally {
            readLock.unlock();
        }
    }

    public void prune() {
        writeLock.lock();
        try {
            pruneCache();
        } finally {
            writeLock.unlock();
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isFull() {
        if (maxSize == 0) {
            return false;
        }
        return cacheMap.size() >= maxSize;
    }

    public void remove(String key) {
        writeLock.lock();
        try {
            cacheMap.remove(key);
        } finally {
            writeLock.unlock();
        }
    }

    public void clear() {
        writeLock.lock();
        try {
            cacheMap.clear();
        } finally {
            writeLock.unlock();
        }
    }

    public int size() {
        return cacheMap.size();
    }

    private void pruneCache() {
        Iterator<CacheEntry> values = cacheMap.values().iterator();
        while (values.hasNext()) {
            CacheEntry entry = values.next();
            if (entry.isExpired()) {
                values.remove();
            }
        }
    }

    static class CacheEntry {
        final String key;
        final Object cachedObject;
        long expiredTime; // time of expire

        CacheEntry(String key, Object object, long ttl) {
            this.key = key;
            this.cachedObject = object;
            this.expiredTime = (ttl == 0) ? 0 : System.currentTimeMillis() + ttl;
        }

        boolean isExpired() {
            if (expiredTime == 0) {
                return false;
            }
            return expiredTime < System.currentTimeMillis();
        }

        Object getObject() {
            return cachedObject;
        }
    }
}
