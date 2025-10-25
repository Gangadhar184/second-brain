package com.example.second_brain.repositories;

import com.example.second_brain.models.Content;
import com.example.second_brain.models.Tag;
import com.example.second_brain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByUser(User user);
    List<Content> findByTitleContainingIgnoreCase(String title);
    List<Content> findByType(String type);


    // Find content by tag
    @Query("SELECT c FROM Content c JOIN c.tags t WHERE t = :tag")
    List<Content> findByTag(@Param("tag") Tag tag);

    // Get all content for a user with a certain tag
    @Query("SELECT c FROM Content c JOIN c.tags t WHERE c.user = :user AND t.title = :tagTitle")
    List<Content> findByUserAndTagTitle(@Param("user") User user, @Param("tagTitle") String tagTitle);

    // Count content per user
    long countByUser(User user);
}
