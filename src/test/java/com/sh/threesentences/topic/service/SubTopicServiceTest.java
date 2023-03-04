package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.SUBTOPIC_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.UNAUTHORIZED_TO_CREATE_TOPIC;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.NOT_FOUND_SUBTOPIC_ID;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.READING_SPACE_ID;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.SUBTOPIC_1;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.SUBTOPIC_ID;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.SUBTOPIC_REQUEST_DTO;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.TOPIC;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.TOPIC_ID;
import static com.sh.threesentences.topic.fixture.SubTopicFixture.USER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.sh.threesentences.common.exception.ForbiddenException;
import com.sh.threesentences.topic.dto.SubTopicResponseDto;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.repository.SubTopicRepository;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.service.AuthorityService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("SubTopicService는 ")
@ExtendWith(MockitoExtension.class)
class SubTopicServiceTest {

    @Mock
    private SubTopicRepository subTopicRepository;

    @Mock
    private AuthorityService authorityService;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private SubTopicService subTopicService;

    @Nested
    @DisplayName("save 메소드는")
    class ContextSaveMethod {

        @Nested
        @DisplayName("정상적으로 요청이 온 경우")
        class ContextSaveValidRequest {

            @DisplayName("서브 토픽을 생성 후 리턴한다.")
            @Test
            void createSubTopic() {

                given(topicRepository.findById(TOPIC_ID)).willReturn(Optional.of(TOPIC));

                given(subTopicRepository.save(any(SubTopic.class))).will(invocation -> {
                    SubTopic subTopic = invocation.getArgument(0);
                    return SubTopic.builder()
                        .id(SUBTOPIC_ID)
                        .name(subTopic.getName())
                        .description(subTopic.getDescription())
                        .startPage(subTopic.getStartPage())
                        .endPage(subTopic.getEndPage())
                        .build();
                });

                SubTopicResponseDto savedTopic = subTopicService.save(SUBTOPIC_REQUEST_DTO, USER_EMAIL,
                    READING_SPACE_ID,
                    TOPIC_ID);

                assertThat(savedTopic.getName()).isEqualTo(SUBTOPIC_REQUEST_DTO.getName());
                assertThat(savedTopic.getDescription()).isEqualTo(SUBTOPIC_REQUEST_DTO.getDescription());
                assertThat(savedTopic.getStartPage()).isEqualTo(SUBTOPIC_REQUEST_DTO.getStartPage());
                assertThat(savedTopic.getEndPage()).isEqualTo(SUBTOPIC_REQUEST_DTO.getEndPage());
            }
        }

        @Nested
        @DisplayName("비정상적으로 요청이 온 경우")
        class ContextSaveInValidRequest {

            @DisplayName("사용자가 해당 ReadingSpace의 관리자가 아니라면 예외를 던진다.")
            @Test
            void cannotCreateSubTopicWithoutAdminAuthority() {
                doThrow(new ForbiddenException(UNAUTHORIZED_TO_CREATE_TOPIC.getMessage())).when(authorityService)
                    .checkUserIsAdminInReadingSpace(READING_SPACE_ID, USER_EMAIL);

                assertThatThrownBy(
                    () -> subTopicService.save(SUBTOPIC_REQUEST_DTO, USER_EMAIL, READING_SPACE_ID, TOPIC_ID))
                    .isInstanceOf(ForbiddenException.class)
                    .hasMessage(UNAUTHORIZED_TO_CREATE_TOPIC.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class ContextDeleteMethod {

        @Test
        @DisplayName("서브 토픽을 삭제한다.")
        void deleteTopic() {
            assertFalse(SUBTOPIC_1.getIsDeleted());

            given(subTopicRepository.findById(SUBTOPIC_1.getId())).willReturn(Optional.of(SUBTOPIC_1));
            subTopicService.delete(READING_SPACE_ID, SUBTOPIC_1.getId(), USER_EMAIL);

            assertTrue(SUBTOPIC_1.getIsDeleted());
        }

        @Test
        @DisplayName("삭제할 토픽이 없으면 예외를 던진다.")
        void deleteTopicNotFound() {
            given(subTopicRepository.findById(NOT_FOUND_SUBTOPIC_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> subTopicService.delete(READING_SPACE_ID, NOT_FOUND_SUBTOPIC_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(SUBTOPIC_NOT_FOUND.getMessage());
        }
    }
}
