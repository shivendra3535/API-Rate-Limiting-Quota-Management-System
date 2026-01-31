package com.cfs.API.Rate.Limiting.Quota.Management.System.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
public class RateLimitingService {
    private final RedisTemplate<String, Integer> redisTemplate;

    public RateLimitingService(RedisTemplate<String,Integer> redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    public void clearAllRateLimitKeys(){
        Set<String> keys= redisTemplate.keys("rate_limit:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
    public Long incrementRequestCount(Long userId){
        String date= LocalDate.now().toString();
        String key="rate_limit:"+userId+":"+date;
        Long count = redisTemplate.opsForValue().increment(key);
        return count;
    }
}
