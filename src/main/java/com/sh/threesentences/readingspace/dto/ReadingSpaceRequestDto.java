package com.sh.threesentences.readingspace.dto;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadingSpaceRequestDto {

    private String name;

    private String description;

    private OpenType openType;

    private String profileImageUrl;

    public ReadingSpace toEntity() {
        return ReadingSpace.builder()
            .name(this.name)
            .description(this.description)
            .openType(this.openType)
            .profileImageUrl(this.profileImageUrl)
            .build();
    }

}
