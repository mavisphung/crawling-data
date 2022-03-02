package com.example.crawlingdata.util;

public interface UrlBuilder {
    public String genKeyword(String keyword);
    public String genCategory(String category);
    public String genLocation(String location);
    public String genSalary(long salaryRangeId);
    
}
