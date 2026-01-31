package com.cfs.API.Rate.Limiting.Quota.Management.System.repository;

import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
