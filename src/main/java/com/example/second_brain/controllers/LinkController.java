package com.example.second_brain.controllers;

import com.example.second_brain.dtos.ContentDto;
import com.example.second_brain.dtos.LinkDto;
import com.example.second_brain.models.User;
import com.example.second_brain.services.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brain")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    // Share all content from the current user
    @PostMapping("/share")
    public ResponseEntity<LinkDto> shareUserContent(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        LinkDto linkDto = linkService.createShareLink(user.getId());
        return ResponseEntity.ok(linkDto);
    }

    //  Share a single content item (individual card)
    @PostMapping("/share/{contentId}")
    public ResponseEntity<LinkDto> shareSingleContent(
            @PathVariable Long contentId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        LinkDto linkDto = linkService.createContentShareLink(user.getId(), contentId);
        return ResponseEntity.ok(linkDto);
    }

    // Access shared content (either single item or all)
    @GetMapping("/{shareHash}")
    public ResponseEntity<List<ContentDto>> getSharedContent(@PathVariable String shareHash) {
        List<ContentDto> contents = linkService.getSharedContent(shareHash);
        return ResponseEntity.ok(contents);
    }
}
