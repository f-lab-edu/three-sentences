package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.COMMENT_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.SENTENCE_NOT_FOUND;

import com.sh.threesentences.topic.dto.CommentRequestDto;
import com.sh.threesentences.topic.dto.CommentResponseDto;
import com.sh.threesentences.topic.entity.Comment;
import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.repository.CommentRepository;
import com.sh.threesentences.topic.repository.SentenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final SentenceRepository sentenceRepository;

    public CommentResponseDto save(CommentRequestDto commentRequestDto) {
        Sentence sentence = sentenceRepository.findById(commentRequestDto.getSentenceId())
            .orElseThrow(() -> new IllegalStateException(SENTENCE_NOT_FOUND.getMessage()));

        Comment newComment = Comment.builder()
            .sentence(sentence)
            .userId(commentRequestDto.getUserId())
            .contents(commentRequestDto.getContents())
            .likes(0)
            .build();

        Comment savedComment = commentRepository.save(newComment);

        return CommentResponseDto.fromEntity(savedComment);
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(COMMENT_NOT_FOUND.getMessage()));
        comment.delete();
    }
}
