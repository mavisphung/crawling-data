package com.example.crawlingdata.repositories;

import com.example.crawlingdata.responses.models.JobItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<JobItem, Integer> {

}
