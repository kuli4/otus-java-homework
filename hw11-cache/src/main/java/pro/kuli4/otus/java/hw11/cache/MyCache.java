package pro.kuli4.otus.java.hw11.cache;

import java.util.WeakHashMap;

public class MyCache<K, V> implements Cache<K, V> {

    private final WeakHashMap<K,V> storage;

    public MyCache() {
        this.storage = new WeakHashMap<>();
    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void remove(K key) {

    }

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void addListener(CacheListener<K, V> listener) {

    }

    @Override
    public void removeListener(CacheListener<K, V> listener) {

    }
}
