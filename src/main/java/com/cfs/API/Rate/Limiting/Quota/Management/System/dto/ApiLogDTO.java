package com.cfs.API.Rate.Limiting.Quota.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;


@Data
@AllArgsConstructor
public class ApiLogDTO {
    private String endpoint;
    private int status;
    private Instant timestamp;
}
