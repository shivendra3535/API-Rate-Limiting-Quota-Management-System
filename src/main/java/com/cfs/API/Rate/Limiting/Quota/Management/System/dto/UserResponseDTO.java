package com.cfs.API.Rate.Limiting.Quota.Management.System.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String plan;
    private Integer dailyQuota;
}

