package com.sh.threesentences.users.service;

import static com.sh.threesentences.users.fixture.UserFixture.DEFAULT_MEMBERSHIP;
import static com.sh.threesentences.users.fixture.UserFixture.DUPLICATE_EMAIL;
import static com.sh.threesentences.users.fixture.UserFixture.ENCRYPTED_PASSWORD;
import static com.sh.threesentences.users.fixture.UserFixture.PASSWORD;
import static com.sh.threesentences.users.fixture.UserFixture.UNUSED_ID;
import static com.sh.threesentences.users.fixture.UserFixture.USER;
import static com.sh.threesentences.users.fixture.UserFixture.USER_ID;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_EMAIL;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_NAME;
import static com.sh.threesentences.users.fixture.UserFixture.VALID_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.readingspace.service.ReadingSpaceService;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.AuthorityType;
import com.sh.threesentences.users.enums.MembershipType;
import com.sh.threesentences.users.exception.EmailDuplicateException;
import com.sh.threesentences.users.exception.UserErrorCode;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("* UserService")
class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private ReadingSpaceService readingSpaceService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("** save method")
    class ContextSaveMethod {

        @DisplayName("└ 사용자를 등록 후 리턴한다.")
        @Test
        void saveUser() {

            given(passwordEncoder.encode(PASSWORD)).willReturn(ENCRYPTED_PASSWORD);

            given(userRepository.save(any(User.class))).will(invocation -> {
                User user = invocation.getArgument(0);
                return User.builder()
                    .id(null)
                    .name(user.getName())
                    .password(user.getPassword())
                    .email(user.getEmail())
                    .authorityType(AuthorityType.BASIC.getName())
                    .membership(MembershipType.FREE)
                    .build();
            });

            given(readingSpaceService.createInitialSpace(any(ReadingSpaceRequestDto.class), any(User.class))).will(
                invocation -> {
                    ReadingSpaceRequestDto readingSpaceRequestDto = invocation.getArgument(0);
                    User user = invocation.getArgument(1);

                    ReadingSpace readingSpace = ReadingSpace.builder()
                        .id(null)
                        .name(readingSpaceRequestDto.getName())
                        .description(readingSpaceRequestDto.getDescription())
                        .openType(readingSpaceRequestDto.getOpenType())
                        .profileImageUrl(readingSpaceRequestDto.getProfileImageUrl())
                        .build();

                    ReadingSpaceMemberRole readingSpaceMemberRole = new ReadingSpaceMemberRole(user, readingSpace,
                        UserRole.ADMIN);

                    return ReadingSpaceResponseDto.fromEntity(readingSpace);
                });

            UserResponseDto savedUser = userService.save(VALID_REQUEST);

            assertThat(savedUser.getEmail()).isEqualTo(VALID_EMAIL);
            assertThat(savedUser.getName()).isEqualTo(VALID_NAME);
            assertThat(savedUser.getMembership()).isEqualTo(DEFAULT_MEMBERSHIP);

            verify(readingSpaceService, times(1)).createInitialSpace(any(ReadingSpaceRequestDto.class),
                any(User.class));
            verify(passwordEncoder, times(1)).encode(any(String.class));

        }
    }


    @Nested
    @DisplayName("** checkEmailDuplicated method")
    class ContextEmailDuplicateCheckMethod {

        @DisplayName("└ 이메일이 중복인 경우 EmailDuplicateException 예외를 던진다.")
        @Test
        void checkDuplicateEmail() {

            given(userRepository.existsByEmail(DUPLICATE_EMAIL)).willReturn(true);

            assertThatThrownBy(() -> userService.checkEmailDuplicated(DUPLICATE_EMAIL))
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
            given(userRepository.findById(USER_ID)).willReturn(Optional.of(USER));
            userService.delete(USER_ID);
            Assertions.assertTrue(USER.getIsDeleted());
        }

        @DisplayName("└ 요청된 사용자가 등록되어 있지 않으면 UserNotFoundException 예외를 던진다.")
        @Test
        void saveUser() {

            given(userRepository.findById(UNUSED_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.delete(UNUSED_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(UserErrorCode.USER_NOT_FOUND.getMessage());
        }
    }

}
