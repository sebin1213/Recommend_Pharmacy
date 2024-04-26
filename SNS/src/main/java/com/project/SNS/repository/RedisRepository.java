package com.project.SNS.repository;

import com.project.SNS.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, User> redisTemplate;

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public Optional<User> getUser(String userName) {
        User data = redisTemplate.opsForValue().get(getKey(userName));
        return Optional.ofNullable(data);
    }


    private String getKey(String userName) {
        return "UID:" + userName;
    }


    public void setUser(User user) {
        String key = getKey(user.getUsername());
        redisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }
}