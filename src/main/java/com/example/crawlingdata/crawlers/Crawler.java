package com.example.crawlingdata.crawlers;

import java.util.List;

import com.example.crawlingdata.responses.models.JobItem;

import lombok.Data;

@Data
public abstract class Crawler {
    
    private String baseUrl;

    public abstract String formatUrl(
        String keyword, 
        String location,
        String companyField, 
        String level, 
        String category,
        double fromSalary,
        double toSalary
    );

    public abstract List<? extends JobItem> crawl();
}
