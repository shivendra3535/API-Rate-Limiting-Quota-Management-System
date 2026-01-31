package com.cfs.API.Rate.Limiting.Quota.Management.System.controller;


import com.cfs.API.Rate.Limiting.Quota.Management.System.dto.UserResponseDTO;
import com.cfs.API.Rate.Limiting.Quota.Management.System.repository.UserRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getPlan().getName().name(),
                        u.getPlan().getDailyQuota()
                ))
                .toList();
    }
}
