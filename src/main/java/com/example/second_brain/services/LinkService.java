package com.example.second_brain.services;

import com.example.second_brain.dtos.ContentDto;
import com.example.second_brain.dtos.LinkDto;
import com.example.second_brain.dtos.TagDto;
import com.example.second_brain.models.Content;
import com.example.second_brain.models.Link;
import com.example.second_brain.models.User;
import com.example.second_brain.repositories.ContentRepository;
import com.example.second_brain.repositories.LinkRepository;
import com.example.second_brain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    //  Base frontend URL for constructing public share links
    private static final String FRONTEND_BASE_URL = "http://localhost:5173/share/";

    public LinkDto createShareLink(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String hash = generateUniqueHash();

        Link link = Link.builder()
                .hash(hash)
                .user(user)
                .content(null) // sharing all user's content
                .build();

        Link savedLink = linkRepository.save(link);

        return LinkDto.builder()
                .id(savedLink.getId())
                .hash(savedLink.getHash())
                .userId(user.getId())
                .shareUrl(FRONTEND_BASE_URL + savedLink.getHash())
                .build();
    }

    public LinkDto createContentShareLink(Long userId, Long contentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));

        if (!content.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this content");
        }

        String hash = generateUniqueHash();

        Link link = Link.builder()
                .hash(hash)
                .user(user)
                .content(content)
                .build();

        Link savedLink = linkRepository.save(link);

        return LinkDto.builder()
                .id(savedLink.getId())
                .hash(savedLink.getHash())
                .userId(user.getId())
                .shareUrl(FRONTEND_BASE_URL + savedLink.getHash())
                .build();
    }
    public ContentDto getContentByHash(String hash) {
        Link link = linkRepository.findByHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid share link"));
        Content content = link.getContent();

        return ContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .link(content.getLink())
                .type(content.getType())
                .tags(content.getTags().stream()
                        .map(tag -> new TagDto(tag.getId(), tag.getTitle()))
                        .collect(Collectors.toSet()))
                .build();
    }


    public List<ContentDto> getSharedContent(String hash) {
        Link link = linkRepository.findByHash(hash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid share link"));

        if (link.getContent() != null) {
            return List.of(toContentDto(link.getContent()));
        }

        List<Content> userContents = contentRepository.findByUser(link.getUser());
        return userContents.stream()
                .map(this::toContentDto)
                .collect(Collectors.toList());
    }

    private String generateUniqueHash() {
        String hash;
        do {
            hash = UUID.randomUUID().toString().substring(0, 8);
        } while (linkRepository.findByHash(hash).isPresent());
        return hash;
    }

    private ContentDto toContentDto(Content content) {
        List<TagDto> tagDtos = content.getTags().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getTitle()))
                .collect(Collectors.toList());

        return ContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .link(content.getLink())
                .type(content.getType())
                .tags(new HashSet<>(tagDtos))
                .build();
    }
}
