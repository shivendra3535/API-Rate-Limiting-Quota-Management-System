package com.cfs.API.Rate.Limiting.Quota.Management.System.controller;

import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.Plan;
import com.cfs.API.Rate.Limiting.Quota.Management.System.repository.PlanRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/plans")
@CrossOrigin(origins = "*")
public class PlanController {

    private final PlanRepository planRepository;

    public PlanController(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @GetMapping
    public List<Plan> getPlans() {
        return planRepository.findAll();
    }
}

