package com.cfs.API.Rate.Limiting.Quota.Management.System.service;


import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.ApiLog;
import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.User;
import com.cfs.API.Rate.Limiting.Quota.Management.System.repository.ApiLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ApiLogService {

    private final ApiLogRepository apiLogRepository;

    public ApiLogService(ApiLogRepository apiLogRepository){
        this.apiLogRepository=apiLogRepository;
    }

    @Async
    public void log(User user, String endpoint, Integer status){
        ApiLog apiLog= new ApiLog();
        apiLog.setUser(user);
        apiLog.setEndpoint(endpoint);
        apiLog.setStatus(status);
        apiLog.setTimestamp(Instant.now());
        apiLogRepository.save(apiLog);
    }
}
