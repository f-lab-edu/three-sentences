package com.sh.threesentences.users.service;

import static com.sh.threesentences.utils.UserFixture.DEFAULT_MEMBERSHIP;
import static com.sh.threesentences.utils.UserFixture.VALID_EMAIL;
import static com.sh.threesentences.utils.UserFixture.VALID_NAME;
import static com.sh.threesentences.utils.UserFixture.VALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.exception.EmailDuplicateException;
import com.sh.threesentences.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("UsersService의 ")
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("save 메소드는")
    class Context_save_method {
        @Nested
        @DisplayName("회원 등록 요청이 정상적으로 온 경우")
        class Context_save_valid_user {

            @DisplayName("사용자를 등록 후 리턴한다.")
            @Test
            void saveUser() {
                UserResponseDto savedUser = userService.save(VALID_REQUEST);

                assertThat(savedUser.getEmail()).isEqualTo(VALID_EMAIL);
                assertThat(savedUser.getName()).isEqualTo(VALID_NAME);
                assertThat(savedUser.getMembership()).isEqualTo(DEFAULT_MEMBERSHIP);
            }
        }
    }

    @Nested
    @DisplayName("checkEmailDuplicated 메소드는")
    class Context_email_duplicate_check_method {
        @Nested
        @DisplayName("이메일이 중복인 경우")
        class Context_save_invalid_user {

            @DisplayName("예외를 던진다.")
            @Test
            void saveUser() {
                userService.save(VALID_REQUEST);
                assertThatThrownBy(() -> userService.checkEmailDuplicated(VALID_EMAIL))
                    .isInstanceOf(EmailDuplicateException.class)
                    .hasMessageContaining("이미 가입된");

            }
        }
    }




}
