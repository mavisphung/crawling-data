package com.example.crawlingdata.responses.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Table
@Entity(name = "job")
public class JobItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String company;

    @Column(name = "company_logo")
    private String companyLogo;

    @Column
    private String location;

    @Column
    private String salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_id", nullable = false)
    @JsonBackReference
    private CrawlHistory history;

    public JobItem(String title, String company, String logo, String location, String salary) {
        this.title = title;
        this.company = company;
        this.companyLogo = logo;
        this.location = location;
        this.salary = salary;
    }
}
