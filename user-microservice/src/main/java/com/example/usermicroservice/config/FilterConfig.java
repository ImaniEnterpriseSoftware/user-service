package com.example.usermicroservice.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean jwtFilter() {
        FilterRegistrationBean filter= new FilterRegistrationBean();
        filter.setFilter(new JwtFilter());
        // When endpoint is provided it will require a JWT token, no endpoint = everything is restricted.
        filter.addUrlPatterns("/api/users");
        filter.addUrlPatterns("/api/users/{id}");
        return filter;
    }
}
