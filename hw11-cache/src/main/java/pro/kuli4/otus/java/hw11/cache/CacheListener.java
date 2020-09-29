package pro.kuli4.otus.java.hw11.cache;

public interface CacheListener<K, V> {
    void notify(K key, V value, String action);
}
