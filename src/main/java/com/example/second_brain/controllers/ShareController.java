package com.example.second_brain.controllers;

import com.example.second_brain.dtos.ContentDto;
import com.example.second_brain.services.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/share")
@RequiredArgsConstructor
public class ShareController {

    private final LinkService linkService;

    @GetMapping("/{hash}")
    public ContentDto getSharedContent(@PathVariable String hash) {
        return linkService.getContentByHash(hash);
    }
}