package com.cfs.API.Rate.Limiting.Quota.Management.System.service;

import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.BlockedRequestsDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.TopUserDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.UsageResponseDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.User;
import com.cfs.API.Rate.Limiting.Quota.Management.System.repository.ApiLogRepository;
import com.cfs.API.Rate.Limiting.Quota.Management.System.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdminService {

    private final RedisTemplate<String, Integer> redisTemplate;
    private final UserRepository userRepository;
    private final ApiLogRepository apiLogRepository;

    public AdminService(RedisTemplate<String, Integer> redisTemplate,
                        UserRepository userRepository,
                        ApiLogRepository apiLogRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.apiLogRepository = apiLogRepository;
    }

    public UsageResponseDTO getUserUsage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String key = "rate_limit:" + userId + ":" + LocalDate.now();
        Integer count = redisTemplate.opsForValue().get(key);

        long used = count == null ? 0 : count;
        int quota = user.getPlan().getDailyQuota();
        long percentage = quota == 0 ? 0 : (used * 100) / quota;

        return new UsageResponseDTO(used, quota, percentage);
    }

    public List<TopUserDTO> getTopUsers(int limit) {
        String today = LocalDate.now().toString();
        Set<String> keys = redisTemplate.keys("rate_limit:*:" + today);

        if (keys == null) return List.of();

        List<TopUserDTO> result = new ArrayList<>();

        for (String key : keys) {
            String[] parts = key.split(":");
            Long userId = Long.parseLong(parts[1]);

            Integer count = redisTemplate.opsForValue().get(key);
            if (count != null) {
                result.add(new TopUserDTO(userId, count));
            }
        }

        return result.stream()
                .sorted((a, b) -> Long.compare(b.getRequests(), a.getRequests()))
                .limit(limit)
                .toList();
    }

    public BlockedRequestsDTO getBlockedRequestsCount() {
        long count = apiLogRepository.countBlockedRequests();
        return new BlockedRequestsDTO(count);
    }
}

