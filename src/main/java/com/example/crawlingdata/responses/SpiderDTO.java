package com.example.crawlingdata.responses;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SpiderDTO {
    private int id;

    private String spiderName;
    
    private String keyword;

    private String location;

    private Timestamp createdAt;
}
