package org.emall.component.cache;

import org.emall.common.serialization.Serialization;
import org.emall.component.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Li Rui
 * @date 2025-09-10
 */
@Component
public class CacheManager {
    @Autowired
    private RedisUtils redisUtils;

    public <T> T read(String key, Type type) {
        return read(key, (sting) -> Serialization.deserialize(sting, type));
    }

    public <T> T read(String key, Function<String, T> mapper) {
        String value = redisUtils.get(key);
        return mapper.apply(value);
    }

    public <T> void write(String key, T t) {
        write(key, t, Serialization::serialize);
    }

    public <T> void write(String key, T value, Function<T, String> mapper) {
        write(key, value, mapper, 0, null);
    }

    public <T> void write(String key, T value, Function<T, String> mapper, long timeout, TimeUnit timeUnit) {
        String str = mapper.apply(value);
        if (timeout <= 0 || timeUnit == null) {
            redisUtils.set(key, str);
        } else {
            redisUtils.set(key, str, timeout, timeUnit);
        }
    }
}