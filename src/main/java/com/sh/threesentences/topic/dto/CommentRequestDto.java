package com.sh.threesentences.topic.dto;

import com.sh.threesentences.topic.entity.Comment;
import com.sh.threesentences.topic.entity.Sentence;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CommentRequestDto {

    @NotNull(message = "댓글을 남길 문장을 선택해주세요.")
    private final Long sentenceId;

    private final Long userId;

    @NotNull(message = "내용을 입력해주세요.")
    private final String contents;

    public Comment toEntity(Sentence sentence) {
        return Comment.builder()
            .contents(this.contents)
            .sentence(sentence)
            .build();
    }

}
