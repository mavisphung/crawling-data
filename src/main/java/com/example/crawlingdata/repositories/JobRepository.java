package com.example.crawlingdata.repositories;

import java.util.List;

import com.example.crawlingdata.responses.models.JobItem;
import com.example.crawlingdata.responses.models.Keyword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<JobItem, Integer> {

    // @Query("SELECT j.* FROM JobItem j")
    // public List<Keyword> findAllJobAndGroupByKeyword();
}
