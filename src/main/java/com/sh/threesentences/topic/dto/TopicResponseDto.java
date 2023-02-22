package com.sh.threesentences.topic.dto;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.topic.entity.Topic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TopicResponseDto {

    private final Long id;
    
    private final String name;

    private final String description;

    private final String naverBookId;

    private final OpenType openType;

    public static TopicResponseDto fromEntity(Topic topic) {
        return new TopicResponseDto(topic.getId(), topic.getName(), topic.getDescription(),
            topic.getNaverBookId(), topic.getOpenType());
    }
}
