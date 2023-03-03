package com.sh.threesentences.topic.dto;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.topic.entity.Topic;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TopicRequestDto {

    @NotNull(message = "이름은 꼭 필요한 값입니다.")
    private final String name;

    private final String description;

    private final String naverBookId;

    @NotNull(message = "공개여부는 기본값이 비공개로 설정되어야 합니다.")
    private final OpenType openType;

    public Topic toEntity(ReadingSpace readingSpace) {
        return Topic.builder()
            .name(this.name)
            .description(this.description)
            .isbn(this.naverBookId)
            .openType(this.openType)
            .readingSpace(readingSpace)
            .build();
    }

}
