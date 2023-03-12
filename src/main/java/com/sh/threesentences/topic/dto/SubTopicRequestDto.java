package com.sh.threesentences.topic.dto;

import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SubTopicRequestDto {

    @NotNull(message = "이름은 꼭 필요한 값입니다.")
    private final String name;

    private final String description;

    private final int startPage;

    private final int endPage;

    public SubTopic toEntity(Topic topic) {
        return SubTopic.builder()
            .name(this.name)
            .description(this.description)
            .startPage(this.startPage)
            .endPage(this.endPage)
            .topic(topic)
            .build();
    }
}
