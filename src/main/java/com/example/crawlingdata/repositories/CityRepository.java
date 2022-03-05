package com.example.crawlingdata.repositories;

import com.example.crawlingdata.responses.models.City;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    
    City findByAliasContaining(String alias);
}
