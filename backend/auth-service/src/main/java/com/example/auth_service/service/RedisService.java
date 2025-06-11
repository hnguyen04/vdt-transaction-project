package com.example.auth_service.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CIRCUIT_BREAKER_NAME = "redisService";

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackSet")
    public void set(String key, Object value, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, duration, unit);
    }

    public void fallbackSet(String key, Object value, long duration, TimeUnit unit, Throwable t) {
        System.out.println("[RedisService fallbackSet] Error setting key: " + key + ", error: " + t.getMessage());
        // Có thể thêm log hoặc gửi alert
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackGet")
    public <T> T get(String key, Class<T> clazz) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        return null;
    }

    public <T> T fallbackGet(String key, Class<T> clazz, Throwable t) {
        System.out.println("[RedisService fallbackGet] Error getting key: " + key + ", error: " + t.getMessage());
        return null;  // fallback trả về null hoặc giá trị mặc định
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackSetList")
    public <T> void setList(String key, List<T> list, long duration, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, list, duration, unit);
    }

    public <T> void fallbackSetList(String key, List<T> list, long duration, TimeUnit unit, Throwable t) {
        System.out.println("[RedisService fallbackSetList] Error setting list key: " + key + ", error: " + t.getMessage());
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackGetList")
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> clazz) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj instanceof List<?>) {
            List<?> rawList = (List<?>) obj;
            if (!rawList.isEmpty() && clazz.isInstance(rawList.get(0))) {
                return (List<T>) rawList;
            }
            if (rawList.isEmpty()) {
                return new ArrayList<>();
            }
        }
        return null;
    }

    public <T> List<T> fallbackGetList(String key, Class<T> clazz, Throwable t) {
        System.out.println("[RedisService fallbackGetList] Error getting list key: " + key + ", error: " + t.getMessage());
        return new ArrayList<>();  // fallback trả về list rỗng
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackHasKey")
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean fallbackHasKey(String key, Throwable t) {
        System.out.println("[RedisService fallbackHasKey] Error checking key existence: " + key + ", error: " + t.getMessage());
        return false;  // fallback trả về false
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackDelete")
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void fallbackDelete(String key, Throwable t) {
        System.out.println("[RedisService fallbackDelete] Error deleting key: " + key + ", error: " + t.getMessage());
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackSetTTL")
    public void setTTL(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    public void fallbackSetTTL(String key, long timeout, TimeUnit unit, Throwable t) {
        System.out.println("[RedisService fallbackSetTTL] Error setting TTL for key: " + key + ", error: " + t.getMessage());
    }

    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackRemoveKeysByPrefix")
    public void removeKeysByPrefix(String prefix) {
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(1000).build();
        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options)) {
            while (cursor.hasNext()) {
                redisTemplate.delete(new String(cursor.next()));
            }
        }
    }

    public void fallbackRemoveKeysByPrefix(String prefix, Throwable t) {
        System.out.println("[RedisService fallbackRemoveKeysByPrefix] Error removing keys with prefix: " + prefix + ", error: " + t.getMessage());
    }
}

