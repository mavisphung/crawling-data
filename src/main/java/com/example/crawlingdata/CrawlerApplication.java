package com.example.crawlingdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args) {

        System.setProperty("webdriver.edge.driver","C:\\coding\\edgedriver_win64\\msedgedriver.exe");
		SpringApplication.run(CrawlerApplication.class, args);
	}

}
