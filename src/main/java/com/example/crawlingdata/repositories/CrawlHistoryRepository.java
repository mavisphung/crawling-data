package com.example.crawlingdata.repositories;

import com.example.crawlingdata.responses.models.CrawlHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrawlHistoryRepository extends JpaRepository<CrawlHistory, Integer> {
    
}
