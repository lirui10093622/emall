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
public class RedisListUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //********** 读 **********//
    public String index(String key, long index) {
        return stringRedisTemplate.opsForList().index(key, index);
    }

    public Long size(String key) {
        return stringRedisTemplate.opsForList().size(key);
    }

    //********** 新增元素 **********//
    public Long leftPush(String key, String value) {
        return stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public Long leftPushAll(String key, String... values) {
        return stringRedisTemplate.opsForList().leftPushAll(key, values);
    }

    public Long leftPushIfPresent(String key, String value) {
        return stringRedisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    public Long rightPush(String key, String value) {
        return stringRedisTemplate.opsForList().rightPush(key, value);
    }

    public void set(String key, long index, String value) {
        stringRedisTemplate.opsForList().set(key, index, value);
    }

    //********** 删除元素 **********//
    public String leftPop(String key) {
        return stringRedisTemplate.opsForList().leftPop(key);
    }

    public String leftPop(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.opsForList().leftPop(key, timeout, timeUnit);
    }

    public String rightPop(String key) {
        return stringRedisTemplate.opsForList().rightPop(key);
    }

    public String rightPop(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.opsForList().rightPop(key, timeout, timeUnit);
    }

    public String rightPopAndLeftPush(String sourceKey, String destinationKey) {
        return stringRedisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey);
    }
}