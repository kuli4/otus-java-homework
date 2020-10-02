package pro.kuli4.otus.java.hw11.cache;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode
public class PrintActionCacheListener<V,K> implements CacheListener<V,K> {

    private static int counter = 1;

    private final int id = counter++;

    @Override
    public void notify(V key, K value, CacheAction action) {
        log.debug("Happens {} on {} = {}", action, key, value);
    }
}
