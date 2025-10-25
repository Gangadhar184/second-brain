package com.example.second_brain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;


import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag extends BaseModel {

    @Column(unique = true, nullable = false)
    private String title;

    //tags should be unique and it is mandatory required true

    // a tag can belong to many contents
    @ManyToMany(mappedBy = "tags")
    private Set<Content> contents;



}
