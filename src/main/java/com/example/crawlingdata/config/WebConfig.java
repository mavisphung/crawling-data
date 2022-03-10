package com.example.crawlingdata.config;

import java.net.MalformedURLException;
import java.net.URL;

import com.example.crawlingdata.util.DummyDatabase;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class WebConfig {
    
    @Bean
    public WebDriver webDriver() throws MalformedURLException {
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setHeadless(true);
        WebDriver webDriver = new RemoteWebDriver(new URL("http://localhost:4444"), edgeOptions);
        return webDriver;
    }

    // @Bean
    // public DummyDatabase getDatabase() {
    //     return new DummyDatabase();
    // }
}
