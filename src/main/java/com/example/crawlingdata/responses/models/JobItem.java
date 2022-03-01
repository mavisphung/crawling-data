package com.example.crawlingdata.responses.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobItem {
    
    private String title;
    private String company;
    private String companyLogo;
    private String deadline;
}
