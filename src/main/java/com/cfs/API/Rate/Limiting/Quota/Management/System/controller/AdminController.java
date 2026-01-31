package com.cfs.API.Rate.Limiting.Quota.Management.System.controller;


import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.BlockedRequestsDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.TopUserDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.UsageResponseDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.service.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/usage/{userId}")
    public UsageResponseDTO getUsage(@PathVariable Long userId) {
        return adminService.getUserUsage(userId);
    }

    @GetMapping("/top-users")
    public List<TopUserDTO> getTopUsers(
            @RequestParam(defaultValue = "5") int limit) {
        return adminService.getTopUsers(limit);
    }

    @GetMapping("/blocked-requests")
    public BlockedRequestsDTO blockedRequests() {
        return adminService.getBlockedRequestsCount();
    }
}
