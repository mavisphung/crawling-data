package com.example.crawlingdata.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    
    @Bean
    public WebDriver webDriver() {
        return new EdgeDriver();
    }
}
