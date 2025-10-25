package com.example.second_brain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link extends BaseModel{

    @Column(nullable = false)
    private String hash;
    //Link will have userId: userRefernece

    //each link belongs to single user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
