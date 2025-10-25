package com.example.second_brain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {

    private Long id;
    private String link;
    private String type;
    private String title;
    private UserSummaryDto user;
    private Set<TagDto> tags;

}