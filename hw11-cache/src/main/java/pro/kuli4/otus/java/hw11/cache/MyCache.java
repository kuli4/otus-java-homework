package pro.kuli4.otus.java.hw11.cache;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements Cache<K, V> {

    private final Map<K,V> storage = new WeakHashMap<>();
    private final List<WeakReference<CacheListener<K,V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
        listeners.forEach((listenerReference) -> {
            CacheListener<K,V> cacheListener = listenerReference.get();
            if(cacheListener != null) {
                cacheListener.notify(key, value, CacheAction.PUT);
            }
        });
    }

    @Override
    public void remove(K key) {
        V removed = storage.remove(key);
        listeners.forEach((listenerReference) -> {
            CacheListener<K,V> cacheListener = listenerReference.get();
            if(cacheListener != null) {
                cacheListener.notify(key, removed, CacheAction.REMOVE);
            }
        });
    }

    @Override
    public V get(K key) {
        V element = storage.get(key);
        listeners.forEach(listenerReference -> {
            CacheListener<K,V> cacheListener = listenerReference.get();
            if(cacheListener != null) {
                cacheListener.notify(key, element, CacheAction.GET);
            }
        });
        return element;
    }

    @Override
    public void addListener(CacheListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(CacheListener<K, V> listener) {
        listeners.forEach(listenerReference -> {
            CacheListener<K,V> cacheListener = listenerReference.get();
            if(cacheListener.equals(listener)) {

            }
        });
    }

    @Override
    public int getSize() {
        return storage.size();
    }
}
