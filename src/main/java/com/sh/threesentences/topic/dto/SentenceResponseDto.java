package com.sh.threesentences.topic.dto;

import com.sh.threesentences.topic.entity.Sentence;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SentenceResponseDto {

    private final Long id;

    private final int page;

    private final String sentence;

    private final String thoughts;

    private final int likes;

    public static SentenceResponseDto fromEntity(Sentence sentence) {
        return new SentenceResponseDto(sentence.getId(), sentence.getPage(), sentence.getSentence(),
            sentence.getThoughts(), sentence.getLikes());
    }

}
