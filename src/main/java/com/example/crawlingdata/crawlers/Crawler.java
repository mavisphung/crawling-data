package com.example.crawlingdata.crawlers;

import java.util.List;

import com.example.crawlingdata.responses.models.JobItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Crawler {
    
    private String baseUrl;
    private String keyword;
    private String jobCategory;
    private String companyField;
    private int minSalary;
    private int maxSalary;
    private String location;
    private int minimumExperience;
    private int position;
    private int workType;

    public abstract String formatUrl(
            String keyword,
            String location,
            String workType,
            String category,
            String companyField,
            String position,
            double fromSalary,
            double toSalary,
            int pageNum
    );

    public abstract List<? extends JobItem> crawl();
}
