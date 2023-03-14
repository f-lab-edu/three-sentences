package com.sh.threesentences.topic.controller;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.DELETE_ADMIN_ONLY;
import static com.sh.threesentences.topic.fixture.TopicFixture.BASIC_READING_SPACE;
import static com.sh.threesentences.topic.fixture.TopicFixture.READING_SPACE_ID;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_ID;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_REQUEST_DTO;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_RESPONSE_DTO;
import static com.sh.threesentences.topic.fixture.TopicFixture.TOPIC_RESPONSE_RESULTS;
import static com.sh.threesentences.topic.fixture.TopicFixture.USER_EMAIL;
import static com.sh.threesentences.utils.MockMvcUtil.OBJECT_MAPPER;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sh.threesentences.auth.config.EmailAuthentication;
import com.sh.threesentences.common.exception.CommonExceptionHandler;
import com.sh.threesentences.common.exception.ForbiddenException;
import com.sh.threesentences.topic.dto.TopicRequestDto;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.fixture.TopicFixture;
import com.sh.threesentences.topic.service.TopicService;
import com.sh.threesentences.users.exception.UserExceptionHandler;
import com.sh.threesentences.utils.MockMvcUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("* TopicController")
@ExtendWith(MockitoExtension.class)
class TopicControllerTest {

    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Object[] controllerAdvices = {new UserExceptionHandler(), new CommonExceptionHandler()};
        mockMvc = MockMvcUtil.createMockMvc(topicController, controllerAdvices);
    }

    @DisplayName("** POST /api/v1/reading-spaces/{reading-space-id}/topics")
    @Nested
    class ContextCreateTopic {

        @DisplayName("└ 토픽 등록에 성공하면 등록 정보와 상태코드 201을 응답한다.")
        @Test
        void createTopic() throws Exception {

            TopicRequestDto topicRequestDto = TOPIC_REQUEST_DTO;

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_EMAIL);

            given(topicService.save(any(TopicRequestDto.class), eq(USER_EMAIL), eq(READING_SPACE_ID))).will(
                invocation -> {
                    TopicRequestDto dto = invocation.getArgument(0);
                    Topic topic = dto.toEntity(BASIC_READING_SPACE);
                    return TopicResponseDto.fromEntity(topic);
                });

            ResultActions result = mockMvc.perform(
                post("/api/v1/reading-spaces/{reading-space-id}/topics", READING_SPACE_ID)
                    .principal(mockPrincipal)
                    .content(OBJECT_MAPPER.writeValueAsString(topicRequestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(topicRequestDto.getName()))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value(topicRequestDto.getDescription()))
                .andExpect(jsonPath("$.openType").value(topicRequestDto.getOpenType().toString()))
                .andDo(print());
        }

        @DisplayName("└ 이름은 항상 값이 존재해야 한다. 입력값이 부적절하면 에러 메시지와 상태코드 400를 응답한다.")
        @ParameterizedTest
        @NullAndEmptySource
        void createTopicWithInvalidName(String invalidName) throws Exception {
            TopicRequestDto request = TopicFixture.createTopicWithInvalidName(invalidName);

            ResultActions result = mockMvc.perform(
                post("/api/v1/reading-spaces/{reading-space-id}/topics", READING_SPACE_ID)
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());

        }
    }


    @DisplayName("** GET /api/v1/reading-spaces/{reading-space-id}/topics/{topic-id}")
    @Nested
    class ContextGetTopic {

        @DisplayName("└ 리딩스페이스의 토픽을 조회한 결과와 상태코드 200을 응답한다.")
        @Test
        void getTopic() throws Exception {

            given(topicService.getTopic(TOPIC_ID)).willReturn(TOPIC_RESPONSE_DTO);

            ResultActions result = mockMvc.perform(
                get("/api/v1/reading-spaces/{reading-space-id}/topics/{topic-id}", READING_SPACE_ID, TOPIC_ID)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(TOPIC_RESPONSE_DTO.getName()))
                .andExpect(jsonPath("$.description").value(TOPIC_RESPONSE_DTO.getDescription()))
                .andExpect(jsonPath("$.isbn").value(TOPIC_RESPONSE_DTO.getIsbn()))
                .andExpect(jsonPath("$.openType").value(TOPIC_RESPONSE_DTO.getOpenType().toString()))
                .andDo(print());
        }
    }

    @DisplayName("** GET /api/v1/reading-spaces/{reading-space-id}/topics")
    @Nested
    class ContextGetTopics {

        @DisplayName("└ 리딩스페이스의 전체 토픽을 조회한 결과와 상태코드 200을 응답한다.")
        @Test
        void getTopic() throws Exception {

            given(topicService.getTopics(READING_SPACE_ID)).willReturn(TOPIC_RESPONSE_RESULTS);

            ResultActions result = mockMvc.perform(
                get("/api/v1/reading-spaces/{reading-space-id}/topics", READING_SPACE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(TOPIC_RESPONSE_RESULTS.size())))
                .andDo(print());
        }
    }

    @DisplayName("** DELETE /api/v1/reading-spaces/{reading-space-id}/topics/{topic-id}")
    @Nested
    class ContextDeleteTopic {

        @DisplayName("└ 토픽 삭제가 정상적으로 이루어지면 상태코드 204를 응답한다.")
        @Test
        void deleteTopic() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_EMAIL);

            ResultActions result = mockMvc.perform(
                delete("/api/v1/reading-spaces/{reading-space-id}/topics/{topic-id}", READING_SPACE_ID, TOPIC_ID)
                    .principal(mockPrincipal)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isNoContent())
                .andDo(print());
        }

        @DisplayName("└ 리딩스페이스의 관리자가 아니면 토픽 삭제에 실패하고 상태코드 403을 응답한다.")
        @Test
        void deleteTopicWithoutAuthority() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_EMAIL);

            doThrow(new ForbiddenException(DELETE_ADMIN_ONLY.getMessage())).when(topicService)
                .delete(TOPIC_ID, READING_SPACE_ID, USER_EMAIL);

            ResultActions result = mockMvc.perform(
                delete("/api/v1/reading-spaces/{reading-space-id}/topics/{topic-id}", READING_SPACE_ID, TOPIC_ID)
                    .principal(mockPrincipal)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isForbidden())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof ForbiddenException))
                .andExpect(r -> assertEquals(r.getResolvedException().getMessage(),
                    DELETE_ADMIN_ONLY.getMessage()))
                .andDo(print());
        }
    }
}


