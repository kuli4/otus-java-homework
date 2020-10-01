package pro.kuli4.otus.java.hw11.cache;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.WeakReference;
import java.util.*;

@Slf4j
public class MyCache<K, V> implements Cache<K, V> {

    private final Map<K, V> storage = new WeakHashMap<>();
    private final List<WeakReference<CacheListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
        listeners.forEach((listenerReference) -> {
            try {
                CacheListener<K, V> cacheListener = listenerReference.get();
                if (cacheListener != null) {
                    cacheListener.notify(key, value, CacheAction.PUT);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public void remove(K key) {
        V removed = storage.remove(key);
        listeners.forEach((listenerReference) -> {
            try {
                CacheListener<K, V> cacheListener = listenerReference.get();
                if (cacheListener != null) {
                    cacheListener.notify(key, removed, CacheAction.REMOVE);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }

    @Override
    public V get(K key) {
        V element = storage.get(key);
        listeners.forEach(listenerReference -> {
            try {
                CacheListener<K, V> cacheListener = listenerReference.get();
                if (cacheListener != null) {
                    cacheListener.notify(key, element, CacheAction.GET);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return element;
    }

    @Override
    public void addListener(CacheListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
        log.debug("Listeners: {}", listeners);
    }

    @Override
    public void removeListener(CacheListener<K, V> listener) {
        listeners.removeIf(listenerReference -> {
            CacheListener<K, V> cacheListener = listenerReference.get();
            return cacheListener == null || cacheListener.equals(listener);
        });
        log.debug("Listeners: {}", listeners);
    }

    @Override
    public int getSize() {
        return storage.size();
    }
}
