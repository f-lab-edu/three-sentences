package com.sh.threesentences.topic.dto;

import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.entity.SubTopic;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SentenceRequestDto {

    private final int page;

    @NotNull(message = "문장은 반드시 입력되어야 합니다.")
    private final String sentence;

    private final String thoughts;

    public Sentence toEntity(SubTopic subtopic, Long userId) {
        return Sentence.builder()
            .page(this.page)
            .sentence(this.sentence)
            .thoughts(this.thoughts)
            .subTopic(subtopic)
            .likes(0)
            .userId(userId)
            .build();
    }
}
