package com.karhacter.movies_webapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.data.domain.PageRequest;
import java.util.List;

@Configuration
public class PaginationConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();

        // Set default page size
        resolver.setFallbackPageable(PageRequest.of(0, 10));

        // Set max page size
        resolver.setMaxPageSize(100);

        // Set page parameter names
        resolver.setPageParameterName("page");
        resolver.setSizeParameterName("size");

        argumentResolvers.add(resolver);
    }
}