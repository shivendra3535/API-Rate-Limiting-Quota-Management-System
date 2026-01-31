package com.cfs.API.Rate.Limiting.Quota.Management.System.controller;

import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.ApiLogDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.repository.ApiLogRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/logs")
@CrossOrigin(origins = "*")
public class ApiLogController {

    private final ApiLogRepository apiLogRepository;

    public ApiLogController(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }

    @GetMapping
    public List<ApiLogDTO> getLogs() {
        return apiLogRepository.findAll().stream()
                .map(l -> new ApiLogDTO(
                        l.getEndpoint(),
                        l.getStatus(),
                        l.getTimestamp()
                ))
                .toList();
    }
}

