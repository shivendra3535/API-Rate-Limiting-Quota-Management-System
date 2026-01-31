package com.cfs.API.Rate.Limiting.Quota.Management.System.filter;

import com.cfs.API.Rate.Limiting.Quota.Management.System.entity.User;
import com.cfs.API.Rate.Limiting.Quota.Management.System.repository.UserRepository;
import com.cfs.API.Rate.Limiting.Quota.Management.System.service.ApiLogService;
import com.cfs.API.Rate.Limiting.Quota.Management.System.service.RateLimitingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-KEY";

    private final UserRepository userRepository;
    private final RateLimitingService rateLimitingService;
    private final ApiLogService apiLogService;


    public ApiKeyAuthFilter(UserRepository userRepository, RateLimitingService rateLimitingService, ApiLogService apiLogService) {
        this.userRepository = userRepository;
        this.rateLimitingService= rateLimitingService;
        this.apiLogService=apiLogService;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1️⃣ Read API key from header
        String apiKey = request.getHeader(API_KEY_HEADER);

        // 2️⃣ Check missing API key
        if (apiKey == null || apiKey.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API Key");
            return;
        }

        // 3️⃣ Validate API key
        Optional<User> userOptional = userRepository.findByApiKey(apiKey);

        if (userOptional.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
            return;
        }

        // 4️⃣ Attach minimal user context to request
        User user = userOptional.get();
        long requestCount = rateLimitingService.incrementRequestCount(user.getId());
        int quota= user.getPlan().getDailyQuota();
        int softLimit= user.getPlan().getSoftLimitThreshold();
        String endpoint= request.getRequestURI();

        if(requestCount>quota){
            response.setStatus(429);
            apiLogService.log(user, endpoint, 429);
            response.getWriter().write("Daily quota exceeded");
            return;
        }

        if(requestCount > softLimit){
            Thread.sleep(300); // artificial delay of 300ms
        }

        request.setAttribute("USER_ID", user.getId());
        request.setAttribute("PLAN_ID", user.getPlan().getId());



        // 5️⃣ Continue request
        filterChain.doFilter(request, response);
        int status= response.getStatus();
        apiLogService.log(user,endpoint, status);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/admin")
                || path.startsWith("/auth")
                || path.startsWith("/swagger")
                || path.startsWith("/v3/api-docs");
    }

}
