package com.cfs.API.Rate.Limiting.Quota.Management.System.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping("/test")
    public String test(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("USER_ID");
        Long planId = (Long) request.getAttribute("PLAN_ID");

        return "Authenticated userId=" + userId + ", planId=" + planId;
    }

}
