package com.example.crawlingdata.responses.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Table
@Entity(name = "work_type")
public class WorkType {
    
    @Id
    private int id;

    @Column
    private String title;
}
