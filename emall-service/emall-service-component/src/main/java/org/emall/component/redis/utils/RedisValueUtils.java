package org.emall.component.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Li Rui
 * @date 2026-01-15
 */
@Slf4j
@Component
public class RedisValueUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //********** 读 **********//
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    //********** 写 **********//
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public void setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, timeUnit);
    }

    public Long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key);
    }

    public Long decrement(String key) {
        return stringRedisTemplate.opsForValue().decrement(key);
    }
}