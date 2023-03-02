package com.sh.threesentences.topic.dto;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.topic.entity.Topic;
import java.util.List;
import java.util.stream.Collectors;
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

    private final List<SubTopicResponseDto> subTopics;

    public static TopicResponseDto fromEntity(Topic topic) {

        List<SubTopicResponseDto> subTopics = topic.getSubTopics()
            .stream()
            .map(SubTopicResponseDto::fromEntity)
            .collect(Collectors.toList());

        return new TopicResponseDto(topic.getId(), topic.getName(),
            topic.getDescription(), topic.getIsbn(), topic.getOpenType(), subTopics);
    }
}
