package pro.kuli4.otus.java.hw11;

import lombok.extern.slf4j.Slf4j;
import pro.kuli4.otus.java.hw11.cache.Cache;
import pro.kuli4.otus.java.hw11.cache.CacheListener;
import pro.kuli4.otus.java.hw11.cache.MyCache;

@Slf4j
public class Launcher {
    public static void main(String[] args) {
        new Launcher().demo();
    }

    private void demo() {
        Cache<Integer, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        CacheListener<Integer, Integer> listener = new CacheListener<Integer, Integer>() {
            @Override
            public void notify(Integer key, Integer value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put(1, 1);

        log.info("getValue:{}", cache.get(1));
        cache.remove(1);
        cache.removeListener(listener);
    }
}
