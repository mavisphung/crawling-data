package com.example.crawlingdata.repositories;

import com.example.crawlingdata.responses.models.WorkType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkTypeRepository extends JpaRepository<WorkType, Integer> {
    
}
