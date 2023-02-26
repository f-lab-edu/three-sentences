package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.UNAUTHORIZED_TO_CREATE_TOPIC;
import static com.sh.threesentences.topic.fixture.TopicFixture.ADMIN_USER_READINGSPACE_MEMBER_ROLE;
import static com.sh.threesentences.topic.fixture.TopicFixture.BASIC_USER_READINGSPACE_MEMBER_ROLE;
import static com.sh.threesentences.topic.fixture.TopicFixture.READING_SPACE_ID;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_ID;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_REQUEST_DTO;
import static com.sh.threesentences.topic.fixture.TopicFixture.USER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.service.AuthorityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TopicService는 ")
@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private AuthorityService authorityService;

    @InjectMocks
    private TopicService topicService;

    @Nested
    @DisplayName("save 메소드는")
    class ContextSaveMethod {

        @Nested
        @DisplayName("정상적으로 요청이 온 경우")
        class ContextSaveValidRequest {

            @DisplayName("토픽을 생성 후 리턴한다.")
            @Test
            void createTopic() {

                given(authorityService.getReadingSpaceMemberRole(READING_SPACE_ID, USER_EMAIL))
                    .willReturn(ADMIN_USER_READINGSPACE_MEMBER_ROLE);

                given(topicRepository.save(any(Topic.class))).will(invocation -> {
                    Topic topic = invocation.getArgument(0);
                    return Topic.builder()
                        .id(TOPIC_ID)
                        .name(topic.getName())
                        .description(topic.getDescription())
                        .naverBookId(topic.getNaverBookId())
                        .openType(topic.getOpenType())
                        .build();
                });

                TopicResponseDto result = topicService.save(TOPIC_REQUEST_DTO, USER_EMAIL, READING_SPACE_ID);

                assertThat(result.getName()).isEqualTo(TOPIC_REQUEST_DTO.getName());
                assertThat(result.getDescription()).isEqualTo(TOPIC_REQUEST_DTO.getDescription());
                assertThat(result.getOpenType()).isEqualTo(TOPIC_REQUEST_DTO.getOpenType());

            }
        }

        @Nested
        @DisplayName("비정상적으로 요청이 온 경우")
        class ContextSaveInValidRequest {

            @DisplayName("사용자가 해당 ReadingSpace의 관리자가 아니라면 예외를 던진다.")
            @Test
            void cannotCreateTopicWithoutAdminAuthority() {

                given(authorityService.getReadingSpaceMemberRole(READING_SPACE_ID, USER_EMAIL))
                    .willReturn(BASIC_USER_READINGSPACE_MEMBER_ROLE);

                doThrow(new IllegalStateException(UNAUTHORIZED_TO_CREATE_TOPIC.getMessage())).when(authorityService)
                    .checkRoleIsAdmin(BASIC_USER_READINGSPACE_MEMBER_ROLE);

                assertThatThrownBy(() -> topicService.save(TOPIC_REQUEST_DTO, USER_EMAIL, READING_SPACE_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage(UNAUTHORIZED_TO_CREATE_TOPIC.getMessage());
            }
        }
    }

}
