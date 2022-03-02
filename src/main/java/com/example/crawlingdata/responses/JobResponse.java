package com.example.crawlingdata.responses;

import java.util.List;

import com.example.crawlingdata.responses.models.JobItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
public class JobResponse {
    
    private int status;
    private String message;
    private int total;
    private String next;
    private Object data;
}
