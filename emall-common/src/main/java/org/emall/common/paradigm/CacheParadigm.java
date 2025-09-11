package org.emall.common.paradigm;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
public class CacheParadigm {

    public static <T> T access(Supplier<T> cacheGetter, Supplier<T> actualityGetter, Consumer<T> cacheSaver) {
        T t = cacheGetter.get();
        if (t != null) {
            return t;
        }
        t = actualityGetter.get();
        if (t != null) {
            cacheSaver.accept(t);
        }
        return t;
    }
}