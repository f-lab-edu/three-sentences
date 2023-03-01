package com.sh.threesentences.topic.dto;


import com.sh.threesentences.topic.entity.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentResponseDto {

    private final Long id;

    private final Long sentence_id;

    private final Long user_id;

    private final String contents;

    private final int likes;

    public static CommentResponseDto fromEntity(Comment comment) {
        return new CommentResponseDto(comment.getId(), comment.getSentence().getId(), comment.getUserId(),
            comment.getContents(), comment.getLikes());
    }

}
