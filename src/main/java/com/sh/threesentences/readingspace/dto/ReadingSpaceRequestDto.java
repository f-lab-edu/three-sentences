package com.sh.threesentences.readingspace.dto;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReadingSpaceRequestDto {

    private final String name;

    private final String description;

    private final OpenType openType;

    private final String profileImageUrl;

    public ReadingSpace toEntity() {
        return ReadingSpace.builder()
            .name(this.name)
            .description(this.description)
            .openType(this.openType)
            .profileImageUrl(this.profileImageUrl)
            .build();
    }

}
