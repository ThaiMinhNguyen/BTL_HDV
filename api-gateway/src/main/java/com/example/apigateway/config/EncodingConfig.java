package com.example.apigateway.config;
   
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Configuration
public class EncodingConfig {
    
    @Bean
    public WebFilter encodingFilter() {
        return (exchange, chain) -> {
            exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return chain.filter(exchange);
        };
    }
}