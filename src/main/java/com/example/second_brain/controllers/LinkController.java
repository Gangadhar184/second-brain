package com.example.second_brain.controllers;

import org.springframework.web.bind.annotation.RestController;

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

    // POST /brain/share → create a new share link
    @PostMapping("/share")
    public ResponseEntity<LinkDto> shareUserContent(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        LinkDto linkDto = linkService.createShareLink(user.getId());
        return ResponseEntity.ok(linkDto);
    }

    // GET /brain/{shareLink}  fetch shared content
    @GetMapping("/{shareLink}")
    public ResponseEntity<List<ContentDto>> getSharedContent(@PathVariable String shareLink) {
        List<ContentDto> contents =  linkService.getSharedContent(shareLink);
        return ResponseEntity.ok(contents);
    }
}