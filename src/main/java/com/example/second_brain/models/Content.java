package com.example.second_brain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content extends BaseModel {

    @Column(nullable = false)
    private String link;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String title;

    //content will have user id for userReference
    //content will have tags for tagReference
    // contentTypes = ['image', 'video', 'article', 'audio']; // Extend as needed

    //Many contents belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Many contents can have many tags
    @ManyToMany
    @JoinTable(
            name = "content_tags",
            joinColumns = @JoinColumn(name = "content_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
