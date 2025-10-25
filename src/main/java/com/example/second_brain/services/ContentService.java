package com.example.second_brain.services;


import com.example.second_brain.dtos.ContentDto;
import com.example.second_brain.dtos.TagDto;
import com.example.second_brain.dtos.UserSummaryDto;
import com.example.second_brain.models.Content;
import com.example.second_brain.models.Tag;
import com.example.second_brain.models.User;
import com.example.second_brain.repositories.ContentRepository;
import com.example.second_brain.repositories.TagRepository;
import com.example.second_brain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ContentService {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    //  Create new content
    public ContentDto createContentForUser(ContentDto dto, User user) {
        Content content = new Content();
        content.setTitle(dto.getTitle());
        content.setLink(dto.getLink());
        content.setType(dto.getType());
        content.setUser(user);

        // Handle tags (same as before)
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            Set<Tag> tags = dto.getTags().stream()
                    .map(tagDto -> tagRepository.findByTitle(tagDto.getTitle())
                            .orElseGet(() -> tagRepository.save(
                                    Tag.builder()
                                            .title(tagDto.getTitle())
                                            .build()
                            ))
                    )
                    .collect(Collectors.toSet());
            content.setTags(tags);
        }

        Content saved = contentRepository.save(content);
        return convertToDto(saved);

    }

    // Get content by ID
    public ContentDto getContentByIdForUser(Long id, User user) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content not found with id " + id));

        // Security check
        if (!content.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: This content does not belong to you");
        }

        return convertToDto(content);
    }


    //  Get all content
    public List<ContentDto> getAllContentsForUser(User user) {
        return contentRepository.findByUser(user)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Update content
    public ContentDto updateContentForUser(Long id, ContentDto dto, User user) {
        Content existing = contentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content not found with id " + id));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: You cannot update others' content");
        }

        existing.setTitle(dto.getTitle());
        existing.setType(dto.getType());
        existing.setLink(dto.getLink());

        if (dto.getTags() != null) {
            Set<Tag> tags = dto.getTags().stream()
                    .map(tagDto -> tagRepository.findByTitle(tagDto.getTitle())
                            .orElseGet(() -> tagRepository.save(
                                    Tag.builder()
                                            .title(tagDto.getTitle())
                                            .build()
                            ))
                    )
                    .collect(Collectors.toSet());
            existing.setTags(tags);
        }

        Content updated = contentRepository.save(existing);
        return convertToDto(updated);
    }

    //Delete content
    public void deleteContentForUser(Long id, User user) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content not found with id " + id));

        if (!content.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Access denied: You cannot delete others' content");
        }

        contentRepository.delete(content);
    }

    // Helper - Entity - DTO
    private ContentDto convertToDto(Content content) {
        Set<TagDto> tagDtos = content.getTags() != null
                ? content.getTags().stream()
                .map(tag -> new TagDto(tag.getId(), tag.getTitle()))
                .collect(Collectors.toSet())
                : Set.of();

        User user = content.getUser();
        UserSummaryDto userSummaryDto = new UserSummaryDto(user.getId(), user.getUsername());

        return ContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .type(content.getType())
                .link(content.getLink())
                .user(userSummaryDto)
                .tags(tagDtos)
                .build();
    }
}
