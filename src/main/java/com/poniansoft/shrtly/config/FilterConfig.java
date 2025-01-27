package com.poniansoft.shrtly.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    public FilterRegistrationBean<FirebaseTokenFilter> firebaseTokenFilter(FirebaseTokenFilter filter) {
        FilterRegistrationBean<FirebaseTokenFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/api/*"); // Apply to all /api endpoints
        return registrationBean;
    }
}
