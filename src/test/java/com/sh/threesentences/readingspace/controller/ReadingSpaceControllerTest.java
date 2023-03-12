package com.sh.threesentences.readingspace.controller;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.DELETE_ADMIN_ONLY;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.MEMBER_IS_STILL_IN_SPACE;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.ALL_PUBLIC_SPACES;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.READING_SPACE_1;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.READING_SPACE_ID;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER1_READING_SPACE;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_1;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_EMAIL;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.VALID_READING_SPACE_REQUEST;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sh.threesentences.auth.config.EmailAuthentication;
import com.sh.threesentences.common.exception.CommonExceptionHandler;
import com.sh.threesentences.common.exception.ForbiddenException;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.service.ReadingSpaceService;
import com.sh.threesentences.users.exception.UserErrorCode;
import com.sh.threesentences.users.exception.UserExceptionHandler;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.utils.MockMvcUtil;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("* ReadingSpaceController")
@ExtendWith(MockitoExtension.class)
class ReadingSpaceControllerTest {

    @Mock
    private ReadingSpaceService readingSpaceService;

    @InjectMocks
    private ReadingSpaceController readingSpaceController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        Object[] controllerAdvices = {new UserExceptionHandler(), new CommonExceptionHandler()};
        mockMvc = MockMvcUtil.createMockMvc(readingSpaceController, controllerAdvices);
    }

    @DisplayName("** GET /api/v1/reading-spaces")
    @Nested
    class ContextGetReadingSpace {

        @DisplayName("└ 전체 공개된 리딩 스페이스와 상태코드 200를 응답한다.")
        @Test
        void getPublicReadingSpace() throws Exception {

            given(readingSpaceService.getPublicReadingSpaces()).willReturn(ALL_PUBLIC_SPACES.stream()
                .map(ReadingSpaceResponseDto::fromEntity)
                .collect(Collectors.toList()));

            ResultActions result = mockMvc.perform(
                get("/api/v1/reading-spaces")
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].openType").value("PUBLIC"))
                .andExpect(jsonPath("$[1].openType").value("PUBLIC"))
                .andExpect(jsonPath("$[2].openType").value("PUBLIC"))
                .andDo(print());
        }
    }

    @DisplayName("** GET /api/v1/reading-spaces/me")
    @Nested
    class ContextGetMyReadingSpace {

        @DisplayName("└ 사용자가 속한 리딩 스페이스와 상태코드 200을 응답한다.")
        @WithMockUser
        @Test
        void getMyReadingSpace() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_EMAIL);

            given(readingSpaceService.getMyReadingSpaces(USER_EMAIL)).willReturn(USER1_READING_SPACE);

            ResultActions result = mockMvc.perform(
                get("/api/v1/reading-spaces/me")
                    .principal(mockPrincipal)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
        }
    }

    @DisplayName("** POST /api/v1/reading-spaces")
    @Nested
    class ContextCreateReadingSpace {

        @DisplayName("└ 리딩 스페이스 등록에 성공하면 등록 정보와 상태코드 201을 응답한다.")
        @Test
        void createReadingSpace() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_EMAIL);
            ReadingSpaceRequestDto requestDto = VALID_READING_SPACE_REQUEST;

            given(readingSpaceService.create(any(ReadingSpaceRequestDto.class), eq(USER_EMAIL))).will(
                invocation -> {
                    ReadingSpaceRequestDto request = invocation.getArgument(0);
                    ReadingSpace readingSpace = request.toEntity();
                    return ReadingSpaceResponseDto.fromEntity(readingSpace);
                });

            ResultActions result = mockMvc.perform(
                post("/api/v1/reading-spaces")
                    .principal(mockPrincipal)
                    .content(OBJECT_MAPPER.writeValueAsString(requestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(requestDto.getName()))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()))
                .andExpect(jsonPath("$.openType").value(requestDto.getOpenType().toString()))
                .andDo(print());
        }
    }

    @DisplayName("** PUT /api/v1/reading-spaces/{id}")
    @Nested
    class ContextUpdateReadingSpace {

        @DisplayName("└ 리딩 스페이스 정보 업데이트에 성공하면 업데이트 정보와 상태코드 200을 응답한다.")
        @Test
        void updateReadingSpace() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_EMAIL);
            ReadingSpaceRequestDto updateRequestDto = VALID_READING_SPACE_REQUEST;

            given(readingSpaceService.update(any(ReadingSpaceRequestDto.class), eq(READING_SPACE_ID), eq(USER_EMAIL)))
                .will(invocation -> {
                    ReadingSpaceRequestDto request = invocation.getArgument(0);
                    ReadingSpace readingSpace = request.toEntity();
                    return ReadingSpaceResponseDto.fromEntity(readingSpace);
                });

            ResultActions result = mockMvc.perform(
                put("/api/v1/reading-spaces/{id}", READING_SPACE_ID)
                    .principal(mockPrincipal)
                    .content(OBJECT_MAPPER.writeValueAsString(updateRequestDto))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(updateRequestDto.getName()))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description").value(updateRequestDto.getDescription()))
                .andExpect(jsonPath("$.openType").value(updateRequestDto.getOpenType().toString()))
                .andDo(print());
        }
    }

    @DisplayName("** DELETE /api/v1/reading-spaces/{id}")
    @Nested
    class ContextDeleteReadingSpace {

        @DisplayName("└ 리딩 스페이스를 삭제하고 상태코드 204을 응답한다.")
        @WithMockUser
        @Test
        void getDeleteReadingSpace() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_EMAIL);

            ResultActions result = mockMvc.perform(
                delete("/api/v1/reading-spaces/{id}", READING_SPACE_1.getId())
                    .principal(mockPrincipal)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isNoContent())
                .andDo(print());
        }

        @DisplayName("└ 사용자 인증 정보가 조회가 안되면 에러 메시지와 상태코드 400을 응답한다.")
        @WithMockUser
        @Test
        void getDeleteReadingSpaceWithInvalidUser() throws Exception {

            Long notExistingId = 999L;

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn("notfound@gmail.com");

            doThrow(new UserNotFoundException()).when(readingSpaceService)
                .delete(notExistingId, "notfound@gmail.com");

            ResultActions result = mockMvc.perform(
                delete("/api/v1/reading-spaces/{id}", notExistingId)
                    .principal(mockPrincipal)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").exists())
                .andExpect(jsonPath("$.httpStatusCode").exists())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof UserNotFoundException))
                .andExpect(r -> assertEquals(r.getResolvedException().getMessage(),
                    UserErrorCode.USER_NOT_FOUND.getMessage()))
                .andDo(print());
        }

        @DisplayName("└ 리딩스페이스의 관리자가 아니면 에러 메시지와 상태코드 403을 응답한다.")
        @WithMockUser
        @Test
        void deleteReadingSpaceWithoutAuthority() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_1.getEmail());

            doThrow(new ForbiddenException(DELETE_ADMIN_ONLY.getMessage())).when(readingSpaceService)
                .delete(READING_SPACE_1.getId(), USER_1.getEmail());

            ResultActions result = mockMvc.perform(
                delete("/api/v1/reading-spaces/{id}", READING_SPACE_1.getId())
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

        @DisplayName("└ 리딩스페이스에 다른 유저가 있다면 삭제가 불가능하다. 에러 메시지와 상태코드 400을 응답한다.")
        @WithMockUser
        @Test
        void deleteReadingSpace() throws Exception {

            EmailAuthentication mockPrincipal = Mockito.mock(EmailAuthentication.class);
            Mockito.when(mockPrincipal.getEmail()).thenReturn(USER_1.getEmail());

            doThrow(new IllegalStateException(MEMBER_IS_STILL_IN_SPACE.getMessage())).when(readingSpaceService)
                .delete(READING_SPACE_1.getId(), USER_1.getEmail());

            ResultActions result = mockMvc.perform(
                delete("/api/v1/reading-spaces/{id}", READING_SPACE_1.getId())
                    .principal(mockPrincipal)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof IllegalStateException))
                .andExpect(r -> assertEquals(r.getResolvedException().getMessage(),
                    MEMBER_IS_STILL_IN_SPACE.getMessage()))
                .andDo(print());
        }
    }
}
