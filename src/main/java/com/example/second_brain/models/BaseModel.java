package com.example.second_brain.models;


import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
