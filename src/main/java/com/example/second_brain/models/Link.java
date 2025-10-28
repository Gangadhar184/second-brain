package com.example.second_brain.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link extends BaseModel{

    @Column(nullable = false, unique = true)
    private String hash;
    //Link will have userId: userRefernece

    //each link belongs to single user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //link can be tied to a single piece of contentn
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = true)
    private Content content;

}
