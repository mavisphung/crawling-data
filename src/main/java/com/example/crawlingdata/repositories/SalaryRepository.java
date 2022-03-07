package com.example.crawlingdata.repositories;

import com.example.crawlingdata.responses.models.Salary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Integer> {
    
}
