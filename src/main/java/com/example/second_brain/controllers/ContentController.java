package com.example.second_brain.controllers;

import com.example.second_brain.dtos.ContentDto;
import com.example.second_brain.models.User;
import com.example.second_brain.services.ContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContentController {

    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ContentDto> createContent(@Valid @RequestBody ContentDto dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ContentDto created = contentService.createContentForUser(dto, user);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDto> getContentById(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ContentDto content = contentService.getContentByIdForUser(id, user);
        return ResponseEntity.ok(content);
    }


    @GetMapping
    public ResponseEntity<List<ContentDto>> getAllContents(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<ContentDto> contents = contentService.getAllContentsForUser(user);
        return ResponseEntity.ok(contents);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentDto> updateContent(@PathVariable Long id, @Valid @RequestBody ContentDto dto, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ContentDto updated = contentService.updateContentForUser(id, dto, user);
        return ResponseEntity.ok(updated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        contentService.deleteContentForUser(id, user);
        return ResponseEntity.noContent().build();
    }
}
