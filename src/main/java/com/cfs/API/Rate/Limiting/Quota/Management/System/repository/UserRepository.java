package com.cfs.API.Rate.Limiting.Quota.Management.System.repository;

import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByApiKey(String apiKey);
}
