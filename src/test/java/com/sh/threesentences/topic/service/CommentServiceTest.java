package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.COMMENT_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.SENTENCE_NOT_FOUND;
import static com.sh.threesentences.topic.fixture.CommentFixture.COMMENT_1;
import static com.sh.threesentences.topic.fixture.CommentFixture.COMMENT_1_ID;
import static com.sh.threesentences.topic.fixture.CommentFixture.COMMENT_REQUEST_DTO;
import static com.sh.threesentences.topic.fixture.CommentFixture.COMMENT_REQUEST_DTO_WITH_NOT_EXISTS_SENTENCE;
import static com.sh.threesentences.topic.fixture.CommentFixture.SENTENCE_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sh.threesentences.topic.dto.CommentResponseDto;
import com.sh.threesentences.topic.entity.Comment;
import com.sh.threesentences.topic.fixture.SentenceFixture;
import com.sh.threesentences.topic.repository.CommentRepository;
import com.sh.threesentences.topic.repository.SentenceRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("* CommentService")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private SentenceRepository sentenceRepository;

    @InjectMocks
    private CommentService commentService;


    @Nested
    @DisplayName("* save method")
    class ContextSaveMethod {

        @Nested
        @DisplayName("[정상 요청]")
        class ContextSaveValidRequest {

            @DisplayName("댓글을 등록 후 리턴한다.")
            @Test
            void createSubTopic() {

                given(sentenceRepository.findById(SENTENCE_1.getId())).willReturn(Optional.of(SENTENCE_1));

                given(commentRepository.save(any(Comment.class))).will(invocation -> {
                    Comment comment = invocation.getArgument(0);
                    return Comment.builder()
                        .id(COMMENT_1_ID)
                        .userId(comment.getUserId())
                        .contents(comment.getContents())
                        .likes(comment.getLikes())
                        .sentence(comment.getSentence())
                        .build();
                });

                CommentResponseDto savedComment = commentService.save(COMMENT_REQUEST_DTO);

                assertThat(savedComment.getContents()).isEqualTo(COMMENT_REQUEST_DTO.getContents());
            }
        }

        @Nested
        @DisplayName("[비정상적 요청]")
        class ContextSaveInValidRequest {

            @DisplayName("댓글을 등록하려는 문장이 존재하지 않으면")
            @Test
            void cannotCreateCommentWhenSentenceIsNotValid() {
                given(sentenceRepository.findById(SentenceFixture.SENTENCE_ID_NOT_EXISTS)).willReturn(Optional.empty());

                assertThatThrownBy(
                    () -> commentService.save(COMMENT_REQUEST_DTO_WITH_NOT_EXISTS_SENTENCE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(SENTENCE_NOT_FOUND.getMessage());
            }
        }
    }


    @Nested
    @DisplayName("** delete method")
    class ContextDeleteMethod {

        @Test
        @DisplayName("댓글을 삭제한다.")
        void deleteTopic() {
            assertFalse(COMMENT_1.getIsDeleted());

            given(commentRepository.findById(COMMENT_1.getId())).willReturn(Optional.of(COMMENT_1));
            commentService.delete(COMMENT_1.getId());

            assertTrue(COMMENT_1.getIsDeleted());
        }

        @Test
        @DisplayName("삭제할 댓글이 없으면 예외를 던진다.")
        void deleteTopicNotFound() {
            given(commentRepository.findById(COMMENT_1_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> commentService.delete(COMMENT_1_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(COMMENT_NOT_FOUND.getMessage());
        }
    }
}
