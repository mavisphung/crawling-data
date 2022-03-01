package com.example.crawlingdata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class WebConfig {
    // @Bean
    // public ObjectMapper customJson() {

    //     return new Jackson2ObjectMapperBuilder()
    //         .indentOutput(true)
    //         .propertyNamingStrategy(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
    //         .build();
    // }
}
