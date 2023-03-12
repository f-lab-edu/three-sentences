package com.sh.threesentences.topic.dto;

import com.sh.threesentences.topic.entity.Sentence;
import java.util.List;
import java.util.stream.Collectors;
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

    private final List<CommentResponseDto> comments;

    public static SentenceResponseDto fromEntity(Sentence sentence) {
        List<CommentResponseDto> comments = sentence.getComments()
            .stream()
            .map(CommentResponseDto::fromEntity)
            .collect(Collectors.toList());
        return new SentenceResponseDto(sentence.getId(), sentence.getPage(), sentence.getSentence(),
            sentence.getThoughts(), sentence.getLikes(), comments);
    }

}
