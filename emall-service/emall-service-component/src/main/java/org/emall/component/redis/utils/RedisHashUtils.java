package org.emall.component.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Li Rui
 * @date 2026-01-15
 */
@Slf4j
@Component
public class RedisHashUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //********** 读 **********//
    public Boolean hasKey(String key, String hashKey) {
        return stringRedisTemplate.opsForHash().hasKey(key, hashKey);
    }

    //********** 写 **********//
    public void put(String key, String field, String value) {
        stringRedisTemplate.opsForHash().put(key, field, value);
    }

    public void putAll(String key, Map<String, String> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }
}