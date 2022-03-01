package com.example.crawlingdata;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args) {

        System.setProperty("webdriver.edge.driver","C:\\coding\\edgedriver_win64\\msedgedriver.exe");
		SpringApplication.run(CrawlerApplication.class, args);
	}

}
