package com.sh.threesentences.users.controller;

import static com.sh.threesentences.users.fixture.UserFixture.DUPLICATE_EMAIL;
import static com.sh.threesentences.users.fixture.UserFixture.EMAIL_DUPLICATE_CHECK_DTO;
import static com.sh.threesentences.users.fixture.UserFixture.RESPONSE;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_EMAIL_DUPLICATE_CHECK_DTO;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_REQUEST;
import static com.sh.threesentences.utils.MockMvcUtil.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sh.threesentences.users.dto.EmailDuplicateCheckDto;
import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.EmailDuplicateException;
import com.sh.threesentences.users.exception.UserErrorCode;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.fixture.UserFixture;
import com.sh.threesentences.users.service.UserService;
import com.sh.threesentences.utils.MockMvcUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("* UserController")
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcUtil.createMockMvc(userController);
    }

    @DisplayName("** POST /api/v1/users")
    @Nested
    class ContextSaveMethod {

        @DisplayName("└ 회원 가입이 성공하면 상태코드 201과 등록된 사용자를 리턴한다.")
        @Test
        void successSaveUser() throws Exception {
            UserRequestDto request = VALID_REQUEST;

            given(userService.save(any(UserRequestDto.class))).will(invocation -> {
                UserRequestDto userRequestDto = invocation.getArgument(0);
                UserRequestDto newUserDto = new UserRequestDto(userRequestDto.getEmail(), userRequestDto.getName(),
                    userRequestDto.getPassword(), userRequestDto.getMembership());
                User user = newUserDto.toEntity(passwordEncoder.encode(newUserDto.getPassword()));
                return UserResponseDto.fromEntity(user);
            });

            ResultActions result = mockMvc.perform(
                post("/api/v1/users")
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.membership").value(request.getMembership().toString()))
                .andDo(print());
        }

        @DisplayName("└ 이름은 한글이면서 20자 이내여야 한다. 입력값이 부적절하면 에러 메시지와 상태코드 400를 응답한다.")
        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"EnglishName", "자바java", "자바!!", "스프링spring!!", "이름은이십자를초과할수없습니다를테스트합니다"})
        void name_request_with_invalid_name(String invalidName) throws Exception {
            // given
            UserRequestDto request = UserFixture.userRequestDtoWithInvalidNameOnly(invalidName);

            ResultActions result = mockMvc.perform(
                post("/api/v1/users")
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());

        }

        @DisplayName("└ 사용되지 않는 HTTP 메소드(GET)로 요청한 경우 상태코드 405를 리턴한다.")
        @Test
        void unusedGetHttpMethod() throws Exception {
            UserRequestDto request = VALID_REQUEST;

            ResultActions result = mockMvc.perform(
                get("/api/v1/users")
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
        }

        @DisplayName("└ 사용되지 않는 HTTP 메소드(DELETE)로 요청한 경우 상태코드 405를 리턴한다.")
        @Test
        void unusedDeleteHttpMethod() throws Exception {
            UserRequestDto request = VALID_REQUEST;

            ResultActions result = mockMvc.perform(
                delete("/api/v1/users")
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
        }

        @DisplayName("└ 사용되지 않는 HTTP 메소드(PATCH)로 요청한 경우 상태코드 405를 리턴한다.")
        @Test
        void unusedPatchHttpMethod() throws Exception {
            UserRequestDto request = VALID_REQUEST;

            ResultActions result = mockMvc.perform(
                patch("/api/v1/users")
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
        }

        @DisplayName("└ 사용되지 않는 HTTP 메소드(PUT)로 요청한 경우 상태코드 405를 리턴한다.")
        @Test
        void unusedPutHttpMethod() throws Exception {
            UserRequestDto request = VALID_REQUEST;

            ResultActions result = mockMvc.perform(
                put("/api/v1/users")
                    .content(OBJECT_MAPPER.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
        }
    }

    @DisplayName("** GET /api/v1/email/duplicate-check")
    @Nested
    class ContextEmailCheckMethod {

        @DisplayName("└ 이메일 중복 체크가 문제 없으면 상태 코드 200을 리턴한다.")
        @Test
        void checkEmailDuplicated() throws Exception {

            doNothing().when(userService).checkEmailDuplicated(VALID_EMAIL_DUPLICATE_CHECK_DTO.getEmail());

            ResultActions result = mockMvc.perform(
                get("/api/v1/users/email/duplicate-check")
                    .content(OBJECT_MAPPER.writeValueAsString(VALID_EMAIL_DUPLICATE_CHECK_DTO))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isOk())
                .andDo(print());
        }

        @DisplayName("└ 이메일 중복이면 에러 메시지와 상태코드 400을 리턴한다.")
        @Test
        void failToCheckEmailDuplicated() throws Exception {

            doThrow(new EmailDuplicateException()).when(userService).checkEmailDuplicated(DUPLICATE_EMAIL);

            ResultActions result = mockMvc.perform(
                get("/api/v1/users/email/duplicate-check")
                    .content(OBJECT_MAPPER.writeValueAsString(EMAIL_DUPLICATE_CHECK_DTO))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").exists())
                .andExpect(jsonPath("$.httpStatusCode").exists())
                .andExpect(result1 -> assertEquals(result1.getResolvedException().getMessage(),
                    UserErrorCode.EMAIL_DUPLICATE.getMessage()))
                .andDo(print());
        }

        @DisplayName("└ 이메일 형식이 잘못된 경우 에러 메시지와 상태코드 400을 응답한다.")
        @ParameterizedTest
        @ValueSource(strings = {"email.com", "id@id@.com", "email"})
        void emailFormatNotValid(String email) throws Exception {

            EmailDuplicateCheckDto emailDuplicateCheckDto = new EmailDuplicateCheckDto(email);

            ResultActions result = mockMvc.perform(
                get("/api/v1/users/email/duplicate-check")
                    .content(OBJECT_MAPPER.writeValueAsString(emailDuplicateCheckDto))
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").exists())
                .andExpect(jsonPath("$.httpStatusCode").exists())
                .andExpect(jsonPath("$.errorMessage").exists())
                .andDo(print());
        }

    }

    @DisplayName("** GET /api/v1/users/{id}")
    @Nested
    class ContextGetMethod {

        @DisplayName("└ id를 받아 사용자 정보와 상태코드 200을 응답한다.")
        @Test
        void getUserById() throws Exception {
            UserResponseDto response = RESPONSE;
            Long id = RESPONSE.getId();

            given(userService.findUser(id)).willReturn(response);

            ResultActions result = mockMvc.perform(
                get("/api/v1/users/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.membership").value(response.getMembership().toString()))
                .andDo(print());
        }

        @DisplayName("└ 존재하지 않는 사용자인 경우 예외를 던진다.")
        @Test
        void getNotExistingUserById() throws Exception {
            Long notExistingUserId = 999L;
            doThrow(new UserNotFoundException()).when(userService).findUser(notExistingUserId);

            ResultActions result = mockMvc.perform(
                get("/api/v1/users/{id}", notExistingUserId)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").exists())
                .andExpect(jsonPath("$.httpStatusCode").exists())
                .andExpect(
                    result1 -> assertEquals(result1.getResolvedException().getMessage(),
                        UserErrorCode.USER_NOT_FOUND.getMessage()))
                .andDo(print());
        }
    }

    @DisplayName("** DELETE /api/v1/users/{id}")
    @Nested
    class ContextDeleteMethod {

        @DisplayName("└ 사용자 삭제가 성공하면 상태코드 204를 응답한다.")
        @Test
        void deleteUserById() throws Exception {
            UserResponseDto response = RESPONSE;
            Long id = RESPONSE.getId();

            doNothing().when(userService).delete(id);

            ResultActions result = mockMvc.perform(
                delete("/api/v1/users/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isNoContent())
                .andDo(print());
        }

        @DisplayName("└ 존재하지 않는 사용자인 경우 예외를 던진다.")
        @Test
        void deleteNotExistingUserById() throws Exception {
            Long notExistingUserId = 999L;
            doThrow(new UserNotFoundException()).when(userService).delete(notExistingUserId);

            ResultActions result = mockMvc.perform(
                delete("/api/v1/users/{id}", notExistingUserId)
                    .contentType(MediaType.APPLICATION_JSON)
            );

            result
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.httpStatus").exists())
                .andExpect(jsonPath("$.httpStatusCode").exists())
                .andExpect(
                    result1 -> assertEquals(result1.getResolvedException().getMessage(),
                        UserErrorCode.USER_NOT_FOUND.getMessage()))
                .andDo(print());
        }

    }
}
