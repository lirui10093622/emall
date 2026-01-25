package org.emall.component.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Li Rui
 * @date 2025-09-09
 */
@Slf4j
@Component
public class RedisKeyUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //********** 读 **********//
    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    //********** 写 **********//
    public Boolean expire(String key, long timeout, TimeUnit timeUnit) {
        return stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    //********** 删 **********//
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }
}