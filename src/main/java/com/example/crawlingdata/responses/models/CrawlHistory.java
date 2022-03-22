package com.example.crawlingdata.responses.models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "crawl_histories")
@Table
@Getter
@Setter
@NoArgsConstructor
public class CrawlHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "spider_name")
    private String spiderName;
    
    @Column(name = "keyword")
    private String keyword;

    @Column(name = "location")
    private String location;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "history", fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<JobItem> jobs;

    public CrawlHistory(String spiderName, String keyword, String location) {
        this.spiderName = spiderName;
        this.keyword = keyword;
        this.location = location;
    }
}
