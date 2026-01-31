package com.cfs.API.Rate.Limiting.Quota.Management.System.scheduler;


import com.cfs.API.Rate.Limiting.Quota.Management.System.service.RateLimitingService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class QuotaResetScheduler {
    private final RateLimitingService redisService;

    public QuotaResetScheduler( RateLimitingService redisService){
        this.redisService= redisService;
    }

    public void resetDailyQuotas(){
        redisService.clearAllRateLimitKeys();
        System.out.println("Daily quotas have been reset.");
    }

}
