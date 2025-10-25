package com.example.second_brain.services;

import com.example.second_brain.dtos.ContentDto;
import com.example.second_brain.dtos.LinkDto;
import com.example.second_brain.dtos.TagDto;
import com.example.second_brain.dtos.UserSummaryDto;
import com.example.second_brain.models.Content;
import com.example.second_brain.models.Link;
import com.example.second_brain.models.User;
import com.example.second_brain.repositories.ContentRepository;
import com.example.second_brain.repositories.LinkRepository;
import com.example.second_brain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;

    //we need hash id which will be uniquee

    private String generateHash() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[6];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // sharelink
    public LinkDto createShareLink(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found") );

        String hash;
        do {
            hash = generateHash();
        }while (linkRepository.findByHash(hash).isPresent());

        Link link = Link.builder().hash(hash).user(user).build();
        Link saved = linkRepository.save(link);
        LinkDto dto = new LinkDto();
        dto.setId(saved.getId());
        dto.setHash(saved.getHash());
        dto.setUserId(saved.getUser().getId());
        return dto;
    }


    public List<ContentDto> getSharedContent(String hash) {
        Link link = linkRepository.findByHash(hash)
                .orElseThrow(() -> new RuntimeException("Invalid or expired share link"));

        User user = link.getUser();
        Set<Content> contents = user.getContents();

        return contents.stream().map(
                content -> {
                    ContentDto dto = new ContentDto();
                    dto.setId(content.getId());
                    dto.setLink(content.getLink());
                    dto.setType(content.getType());
                    dto.setTitle(content.getTitle());
                    dto.setUser(new UserSummaryDto(user.getId(), user.getUsername()));

                    Set<TagDto> tagDtos = content.getTags().stream().map(tag -> {
                        TagDto tagDto = new TagDto();
                        tagDto.setId(tag.getId());
                        tagDto.setTitle(tag.getTitle());
                        return tagDto;
                    }).collect(Collectors.toSet());

                    dto.setTags(tagDtos);
                    return dto;
                }
        ).collect(Collectors.toList());
    }

}



