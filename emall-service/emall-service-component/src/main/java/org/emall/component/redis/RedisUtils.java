package org.emall.component.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Li Rui
 * @date 2025-09-09
 */
@Component
public class RedisUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        Function<String, List<T>> mapper = (str) -> StringUtils.isNotBlank(str) ? JSON.parseArray(str, clazz) : null;
        return get(key, mapper);
    }

    public <T> T getObject(String key, Class<? extends T> clazz) {
        Function<String, T> mapper = (str) -> StringUtils.isNotBlank(str) ? JSON.parseObject(str, clazz) : null;
        return get(key, mapper);
    }

    public <T> T get(String key, Function<String, T> mapper) {
        String value = stringRedisTemplate.opsForValue().get(key);
        return mapper.apply(value);
    }

    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public <T> void set(String key, T value, Function<T, String> mapper) {
        String str = mapper.apply(value);
        stringRedisTemplate.opsForValue().set(key, str);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public <T> void set(String key, T value, Function<T, String> mapper, long timeout, TimeUnit timeUnit) {
        String str = mapper.apply(value);
        stringRedisTemplate.opsForValue().set(key, str, timeout, timeUnit);
    }

    public void setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    public Long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public Long decrement(String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }
}