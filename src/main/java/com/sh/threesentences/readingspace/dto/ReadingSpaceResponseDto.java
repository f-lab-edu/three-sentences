package com.sh.threesentences.readingspace.dto;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ReadingSpaceResponseDto {

    private Long id;

    private String name;

    private String description;

    private OpenType openType;

    private String profileImageUrl;

    @Builder
    public ReadingSpaceResponseDto(Long id, String name, String description, OpenType openType,
        String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.openType = openType;
        this.profileImageUrl = profileImageUrl;
    }

    public static ReadingSpaceResponseDto fromEntity(ReadingSpace readingSpace) {
        return ReadingSpaceResponseDto.builder()
            .id(readingSpace.getId())
            .name(readingSpace.getName())
            .description(readingSpace.getDescription())
            .openType(readingSpace.getOpenType())
            .profileImageUrl(readingSpace.getProfileImageUrl())
            .build();
    }
}
