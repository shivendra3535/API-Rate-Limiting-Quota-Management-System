package com.cfs.API.Rate.Limiting.Quota.Management.System.repository;

import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {


}
