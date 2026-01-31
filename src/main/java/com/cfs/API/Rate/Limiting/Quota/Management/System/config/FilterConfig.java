package com.cfs.API.Rate.Limiting.Quota.Management.System.config;

import com.cfs.API.Rate.Limiting.Quota.Management.System.filter.ApiKeyAuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ApiKeyAuthFilter> apiKeyFilter(ApiKeyAuthFilter filter) {
        FilterRegistrationBean<ApiKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*"); // protect APIs only
        registrationBean.setOrder(1); // very early
        return registrationBean;
    }
}
