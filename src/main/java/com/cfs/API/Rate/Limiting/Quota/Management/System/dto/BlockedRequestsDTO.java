package com.cfs.API.Rate.Limiting.Quota.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockedRequestsDTO {
    private long blockedRequests;
}
