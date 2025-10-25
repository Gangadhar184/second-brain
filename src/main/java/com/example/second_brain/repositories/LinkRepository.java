package com.example.second_brain.repositories;

import com.example.second_brain.models.Link;
import com.example.second_brain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByHash(String hash);
    List<Link> findByUser(User user);

    // Count links for a user
    long countByUser(User user);

    // Search by partial hash (useful for autocomplete)
    List<Link> findByHashContainingIgnoreCase(String hash);
}
