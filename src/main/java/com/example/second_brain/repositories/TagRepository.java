package com.example.second_brain.repositories;

import com.example.second_brain.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTitle(String title);
    boolean existsByTitle(String title);

    // Search for tags containing a string (case-insensitive)
    List<Tag> findByTitleContainingIgnoreCase(String partialTitle);

    // Get top N tags by number of associated contents
    @Query("SELECT t FROM Tag t ORDER BY size(t.contents) DESC")
    List<Tag> findTopTags(org.springframework.data.domain.Pageable pageable);
}
