package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.UNAUTHORIZED_TO_CREATE_TOPIC;
import static com.sh.threesentences.topic.fixture.TopicFixture.ADMIN_USER_READINGSPACE_MEMBER_ROLE;
import static com.sh.threesentences.topic.fixture.TopicFixture.BASIC_USER_READINGSPACE_MEMBER_ROLE;
import static com.sh.threesentences.topic.fixture.TopicFixture.NOT_FOUND_TOPIC_ID;
import static com.sh.threesentences.topic.fixture.TopicFixture.READING_SPACE_ID;
import static com.sh.threesentences.topic.fixture.TopicFixture.SUBTOPIC_1;
import static com.sh.threesentences.topic.fixture.TopicFixture.SUBTOPIC_2;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_2;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_3;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_4;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_ID;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_REQUEST_DTO;
import static com.sh.threesentences.topic.fixture.TopicFixture.USER_EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.service.AuthorityService;
import java.util.List;
import java.util.Optional;
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
                        .isbn(topic.getIsbn())
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

    @Nested
    @DisplayName("delete 메소드는")
    class ContextDeleteMethod {

        @Test
        @DisplayName("토픽과 서브 토픽을 모두 삭제 시킨다.")
        void deleteTopic() {
            assertFalse(TOPIC.getIsDeleted());
            assertFalse(SUBTOPIC_1.getIsDeleted());
            assertFalse(SUBTOPIC_2.getIsDeleted());

            given(topicRepository.findById(TOPIC_ID)).willReturn(Optional.of(TOPIC));

            topicService.delete(TOPIC_ID, READING_SPACE_ID, USER_EMAIL);

            assertTrue(TOPIC.getIsDeleted());

            // TODO
            // assertTrue(SUBTOPIC_1.getIsDeleted());
            // assertTrue(SUBTOPIC_2.getIsDeleted());
        }

        @Test
        @DisplayName("삭제할 토픽이 없으면 예외를 던진다.")
        void deleteTopicNotFound() {
            given(topicRepository.findById(NOT_FOUND_TOPIC_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> topicService.delete(NOT_FOUND_TOPIC_ID, READING_SPACE_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(TOPIC_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("getTopic 메소드는")
    class ContextGetTopicMethod {

        @Test
        @DisplayName("ID에 해당하는 토픽 1개를 조회한다.")
        void getTopic() {
            given(topicRepository.findById(TOPIC_ID)).willReturn(Optional.of(TOPIC));

            TopicResponseDto findTopic = topicService.getTopic(TOPIC_ID);

            assertThat(TOPIC.getName()).isEqualTo(findTopic.getName());
            assertThat(TOPIC.getDescription()).isEqualTo(findTopic.getDescription());
            assertThat(TOPIC.getIsbn()).isEqualTo(findTopic.getIsbn());
        }

        @Test
        @DisplayName("ID에 해당하는 토픽이 없으면 예외를 던진다.")
        void getTopicNotFound() {
            given(topicRepository.findById(NOT_FOUND_TOPIC_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> topicService.getTopic(NOT_FOUND_TOPIC_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(TOPIC_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("getTopics 메소드는")
    class ContextGetTopicsMethod {

        @Test
        @DisplayName("ReadingSpace에 속하는 모든 토픽을 조회한다.")
        void getTopics() {
            assertFalse(TOPIC.getIsDeleted());

            given(topicRepository.findByReadingSpaceId(READING_SPACE_ID)).willReturn(
                List.of(TOPIC_2, TOPIC_3, TOPIC_4));

            List<TopicResponseDto> topics = topicService.getTopics(READING_SPACE_ID);

            assertThat(topics).hasSize(3);
        }
    }

    @Nested
    @DisplayName("getPublicTopics 메소드는")
    class ContextGetPublicTopicsMethod {

        @Test
        @DisplayName("공개된 모든 토픽을 조회한다.")
        void getPublicTopics() {
            assertFalse(TOPIC.getIsDeleted());

            List<Topic> publicTopics = List.of(TOPIC, TOPIC_2, TOPIC_3);

            given(topicRepository.findAllByOpenType(OpenType.PUBLIC)).willReturn(
                publicTopics);

            List<TopicResponseDto> topics = topicService.getPublicTopics();

            assertThat(topics).hasSize(3);
            assertThat(topics).extracting("openType").containsOnly(OpenType.PUBLIC);
        }
    }
}
