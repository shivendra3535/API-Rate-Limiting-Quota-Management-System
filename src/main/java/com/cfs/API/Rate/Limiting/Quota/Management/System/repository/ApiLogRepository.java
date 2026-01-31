package com.cfs.API.Rate.Limiting.Quota.Management.System.repository;

import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {

    @Query("""
SELECT COUNT(a)
FROM ApiLog a
WHERE a.status IN (401, 429)
""")
    long countBlockedRequests();
}
