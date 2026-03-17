package org.emall.component.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Li Rui
 * @date 2026-01-15
 */
@Slf4j
@Component
public class RedisSetUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //********** 读 **********//
    public Set<String> members(String key) {
        return stringRedisTemplate.opsForSet().members(key);
    }

    public Long size(String key) {
        return stringRedisTemplate.opsForSet().size(key);
    }

    public Boolean isMember(String key, String value) {
        return stringRedisTemplate.opsForSet().isMember(key, value);
    }

    //********** 添加元素 **********//
    public Long add(String key, String value) {
        return stringRedisTemplate.opsForSet().add(key, value);
    }

    public Long add(String key, String... values) {
        return stringRedisTemplate.opsForSet().add(key, values);
    }

    //********** 删除元素 **********//
    public Long remove(String key, String value) {
        return stringRedisTemplate.opsForSet().remove(key, value);
    }
}