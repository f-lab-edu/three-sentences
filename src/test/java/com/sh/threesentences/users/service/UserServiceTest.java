package com.sh.threesentences.users.service;

import static com.sh.threesentences.users.fixture.UserFixture.DEFAULT_MEMBERSHIP;
import static com.sh.threesentences.users.fixture.UserFixture.UNUSED_ID;
import static com.sh.threesentences.users.fixture.UserFixture.USER_ID;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_EMAIL;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_NAME;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.sh.threesentences.readingspace.repository.ReadingSpaceRepository;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.EmailDuplicateException;
import com.sh.threesentences.users.exception.UserErrorCode;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("* UserService")
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReadingSpaceRepository readingSpaceRepository;

    @Autowired
    UserReadingSpaceRepository userReadingSpaceRepository;

    @Autowired
    UserService userService;

    @AfterEach
    void cleanup() {
        userReadingSpaceRepository.deleteAll();
        userRepository.deleteAll();
        readingSpaceRepository.deleteAll();
    }

    @Nested
    @DisplayName("** save method")
    class ContextSaveMethod {

        @DisplayName("└ 사용자를 등록 후 리턴한다.")
        @Test
        void saveUser() {
            UserResponseDto savedUser = userService.save(VALID_REQUEST);

            assertThat(savedUser.getEmail()).isEqualTo(VALID_EMAIL);
            assertThat(savedUser.getName()).isEqualTo(VALID_NAME);
            assertThat(savedUser.getMembership()).isEqualTo(DEFAULT_MEMBERSHIP);
        }
    }


    @Nested
    @DisplayName("** checkEmailDuplicated method")
    class ContextEmailDuplicateCheckMethod {

        @DisplayName("└ 이메일이 중복인 경우 EmailDuplicateException 예외를 던진다.")
        @Test
        void saveUser() {
            userService.save(VALID_REQUEST);
            assertThatThrownBy(() -> userService.checkEmailDuplicated(VALID_EMAIL))
                .isInstanceOf(EmailDuplicateException.class)
                .hasMessageContaining(UserErrorCode.EMAIL_DUPLICATE.getMessage());
        }
    }

    @Nested
    @DisplayName("** delete method")
    class ContextDeleteMethod {

        @DisplayName("└ 요청된 사용자가 등록되어 있으면 사용자 엔티티의 is_delete 컬럼을 true로 변경한다.")
        @Test
        void deleteUser() {
            userService.save(VALID_REQUEST);
            userService.delete(USER_ID);
            User deletedUser = userRepository.findById(USER_ID)
                .orElseThrow(UserNotFoundException::new);

            assertThat(deletedUser.getIsDeleted()).isTrue();
        }

        @DisplayName("└ 요청된 사용자가 등록되어 있지 않으면 UserNotFoundException 예외를 던진다.")
        @Test
        void saveUser() {
            assertThatThrownBy(() -> userService.delete(UNUSED_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(UserErrorCode.USER_NOT_FOUND.getMessage());
        }
    }

}
