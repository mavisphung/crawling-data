package com.example.crawlingdata.responses.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private int deadline;

    @Column
    private String location;

    @Column
    private String salary;

    @Column
    private String updatedAt;

    public JobItem(String title, String company, String logo, int deadline, String location, String salary, String time) {
        this.title = title;
        this.company = company;
        this.companyLogo = logo;
        this.deadline = deadline;
        this.location = location;
        this.salary = salary;
        this.updatedAt = time;
    }
}
