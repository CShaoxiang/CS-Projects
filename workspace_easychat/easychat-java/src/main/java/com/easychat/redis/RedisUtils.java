package com.easychat.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component("redisUtils")
public class RedisUtils<V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * Delete cache Redis keys
     *
     * @param key Deletes one or more cache keys from Redis.
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    public V get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * Retrieves a value from Redis by key.
     *
     * @param key   Redis key
     * @param value Value to store
     * @return true false
     */
    public boolean set(String key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("set redisKey:{},value:{}false", key, value);
            return false;
        }
    }

    /**
     * Store a value in Redis with an expiration time.
     *
     * @param key   Redis key
     * @param value Value to Store
     * @param time  Expiration time in seconds. If time <= 0, the key will not expire.
     * @return true false
     */
    public boolean setex(String key, V value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("Failed to set Redis key with expiration: {},value: {}", key, value);
            return false;
        }
    }
    /**
     * Updates the expiration time of a Redis key.
     *
     * @param key  Redis key.
     * @param time Expiration time in seconds.
     * @return true if successful, false otherwise.
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.error("Failed to update expiration for Redis key: {}", key);
            return false;
        }
    }

    /**
     * Retrieves all elements from a Redis list.
     */
    public List<V> getQueueList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Pushes a value into the left side of a Redis list and optionally sets expiration.
     */
    public boolean lpush(String key, V value, long time) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("Failed to push value into Redis list: {}", key);
            return false;
        }
    }

    /**
     * Removes a matching element from a Redis list.
     *
     * @return Number of removed elements.
     */
    public long remove(String key, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, 1, value);
            return remove;
        } catch (Exception e) {
            logger.error("Failed to remove value from Redis list: {}", key);
            return 0;
        }
    }

    /**
     * Pushes multiple values into a Redis list and optionally sets expiration.
     */
    public boolean lpushAll(String key, List<V> values, long time) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("Failed to push multiple values into Redis list: {}", key);
            return false;
        }
    }

}
