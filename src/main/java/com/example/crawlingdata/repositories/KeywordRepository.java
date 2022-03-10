package com.example.crawlingdata.repositories;

import java.util.List;

import com.example.crawlingdata.responses.models.Keyword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
    
    // @Query("SELECT j.* FROM JobItem j, Keyword k GROUP BY k.name")
    // public List<Keyword> findAllJobAndGroupByKeyword();
}
