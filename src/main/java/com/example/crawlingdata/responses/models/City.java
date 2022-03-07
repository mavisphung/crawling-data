package com.example.crawlingdata.responses.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "city")
public class City {
    
    @Id
    private int id;

    @Column
    private String name;

    @Column
    private String alias;
}
