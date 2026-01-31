package com.cfs.API.Rate.Limiting.Quota.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsageResponseDTO {
    private long used;
    private int quota;
    private long percentage;
}
