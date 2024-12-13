package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACCESS_TOKEN_PREFIX = "access_token:";

    @Value("${jwt.expiration.time}")
    private long expirationTime;

    public void saveAccessToken(String token) {
        String key = ACCESS_TOKEN_PREFIX + token;
        redisTemplate.opsForValue().set(key, token, expirationTime, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenExpired(String token) {
        String key = ACCESS_TOKEN_PREFIX + token;
        return redisTemplate.hasKey(key);
    }

    public void deleteAccessToken(String token) {
        String key = ACCESS_TOKEN_PREFIX + token;
        redisTemplate.delete(key);
    }
}
