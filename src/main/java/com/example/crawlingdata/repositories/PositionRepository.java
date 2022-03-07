package com.example.crawlingdata.repositories;

import com.example.crawlingdata.responses.models.Position;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {
    
}
