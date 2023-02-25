package com.sh.threesentences.topic.dto;

import com.sh.threesentences.topic.entity.SubTopic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubTopicResponseDto {

    private final Long id;

    private final String name;

    private final String description;

    private final int startPage;

    private final int endPage;

    public static SubTopicResponseDto fromEntity(SubTopic subtopic) {
        return new SubTopicResponseDto(subtopic.getId(), subtopic.getName(), subtopic.getDescription(),
            subtopic.getStartPage(), subtopic.getEndPage());
    }
}
