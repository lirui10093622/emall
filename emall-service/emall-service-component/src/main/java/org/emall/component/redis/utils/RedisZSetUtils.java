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
public class RedisZSetUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //********** 读 **********//
    public Set<String> range(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().range(key, start, end);
    }

    public Set<String> rangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public Set<String> reverseRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    public Set<String> reverseRangeByScore(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    public Long rank(String key, String value) {
        return stringRedisTemplate.opsForZSet().rank(key, value);
    }

    public Long reverseRank(String key, String value) {
        return stringRedisTemplate.opsForZSet().reverseRank(key, value);
    }

    public Double score(String key, String value) {
        return stringRedisTemplate.opsForZSet().score(key, value);
    }

    public Long count(String key, double min, double max) {
        return stringRedisTemplate.opsForZSet().count(key, min, max);
    }

    public Long size(String key) {
        return stringRedisTemplate.opsForZSet().size(key);
    }

    public Long zCard(String key) {
        return stringRedisTemplate.opsForZSet().zCard(key);
    }

    //********** 添加元素 **********//
    public Boolean add(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }


    //********** 删除元素 **********//
    public Long remove(String key, String value) {
        return stringRedisTemplate.opsForZSet().remove(key, value);
    }

    public Long removeRange(String key, long start, long end) {
        return stringRedisTemplate.opsForZSet().removeRange(key, start, end);
    }

    public void removeRangeByScore(String key, double min, double max) {
        stringRedisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    //********** 改 **********//
    public void incrementScore(String key, String value, double delta) {
        stringRedisTemplate.opsForZSet().incrementScore(key, value, delta);
    }
}