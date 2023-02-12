package com.sh.threesentences.readingspace.service;


import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.MEMBER_IS_STILL_IN_SPACE;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.NO_ADMIN_IN_SPACE;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.ALL_READING_SPACES;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.MY_READING_SPACES_SIZE;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.PUBLIC_READING_SPACES_SIZE;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.READING_SPACE_ID;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.UNUSED_READING_SPACE_ID;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_EMAIL;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_ID;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_READING_MAPPINGS;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_READING_MAPPINGS_FOR_DELETE_2;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.VALID_READING_SPACE_REQUEST;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.VALID_READING_SPACE_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.repository.ReadingSpaceRepository;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.users.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReadingSpaceService는")
class ReadingSpaceServiceTest {

    @Mock
    private ReadingSpaceRepository readingSpaceRepository;

    @Mock
    UserReadingSpaceRepository userReadingSpaceRepository;

    @InjectMocks
    private ReadingSpaceService readingSpaceService;

    ReadingSpaceRequestDto validReadingSpaceRequestDto;
    ReadingSpaceResponseDto validReadingSpaceResponseDto;

    @BeforeEach
    void setUp() {
        validReadingSpaceRequestDto = VALID_READING_SPACE_REQUEST;
        validReadingSpaceResponseDto = VALID_READING_SPACE_RESPONSE;
    }

    @Nested
    @DisplayName("create 메소드는")
    class ContextCreatedMethod {

        @Nested
        @DisplayName("정상적으로 요청이 온 경우")
        class ContextSaveValidReadingspace {

            @DisplayName("ReadingSpace를 생성 후 리턴한다.")
            @Test
            void createReadingSpace() {
                given(readingSpaceRepository.save(any(ReadingSpace.class))).will(invocation -> {
                    ReadingSpace readingSpace = invocation.getArgument(0);
                    return ReadingSpace.builder()
                        .id(null)
                        .name(readingSpace.getName())
                        .description(readingSpace.getDescription())
                        .openType(readingSpace.getOpenType())
                        .profileImageUrl(readingSpace.getProfileImageUrl())
                        .build();
                });

                // TODO 임시 사용자. 추후 삭제
                User user = new User();
                ReadingSpaceResponseDto readingSpaceResponseDto = readingSpaceService.create(
                    validReadingSpaceRequestDto, user.getEmail());

                assertThat(readingSpaceResponseDto.getName()).isEqualTo(validReadingSpaceResponseDto.getName());
                assertThat(readingSpaceResponseDto.getDescription()).isEqualTo(
                    validReadingSpaceResponseDto.getDescription());
                assertThat(readingSpaceResponseDto.getOpenType()).isEqualTo(validReadingSpaceResponseDto.getOpenType());
                assertThat(readingSpaceResponseDto.getProfileImageUrl()).isEqualTo(
                    validReadingSpaceResponseDto.getProfileImageUrl());
            }
        }
    }

    @Nested
    @DisplayName("getPublicReadingSpaces 메소드는")
    class ContextGetPublicReadingSpacesMethod {

        @DisplayName("공개된 전체 ReadingSpace를 조회한다.")
        @Test
        void getPublicReadingSpaces() {
            given(readingSpaceRepository.findAllByOpenType(OpenType.PUBLIC)).willReturn(
                ALL_READING_SPACES
                    .stream()
                    .filter(s -> s.getOpenType().equals(OpenType.PUBLIC))
                    .collect(Collectors.toList())
            );

            List<ReadingSpaceResponseDto> readingSpaces = readingSpaceService.getPublicReadingSpaces();

            assertThat(readingSpaces).hasSize(PUBLIC_READING_SPACES_SIZE)
                .extracting(ReadingSpaceResponseDto::getOpenType)
                .containsOnly(OpenType.PUBLIC);
        }
    }

    @Nested
    @DisplayName("update 메소드는")
    class ContextUpdateMethod {

        @DisplayName("ReadingSpace 정보를 업데이트 한다.")
        @Test
        void update_readingspace() {
            // JPA의 변경 감지에 의해 업데이트가 되는데,
            // 서비스가 잘 동작하는지를 목적으로 하는 테스트에서 해당 테스트를 하는게 맞을까?
        }

        @DisplayName("업데이트할 ReadingSpace가 없는 경우, 예외를 던진다.")
        @Test
        void not_found_readingspace_for_update() {
            given(readingSpaceRepository.findById(UNUSED_READING_SPACE_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> readingSpaceService.update(VALID_READING_SPACE_REQUEST, UNUSED_READING_SPACE_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(READING_SPACE_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("getMyReadingSpaces 메소드는")
    class ContextGetMyReadingSpaceMethod {

        @DisplayName("특정 사용자의 readingspace를 조회한다.")
        @Test
        void getMyReadingSpaces() {
            given(userReadingSpaceRepository.findByUserId(USER_ID)).willReturn(
                USER_READING_MAPPINGS
            );

            List<ReadingSpaceResponseDto> myReadingSpaces = readingSpaceService.getMyReadingSpaces(USER_EMAIL);

            assertThat(myReadingSpaces).hasSize(MY_READING_SPACES_SIZE);
        }
    }

    @Nested
    @DisplayName("delete 메소드는")
    class ContextDeleteMethod {

        @DisplayName("ReadingSpace를 삭제한다.")
        @Test
        void delete() {
            given(userReadingSpaceRepository.findByReadingSpaceId(READING_SPACE_ID)).willReturn(
                USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET
            );

            readingSpaceService.delete(READING_SPACE_ID, USER_EMAIL);
            boolean deleted = USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET.get(0).getReadingSpace().isDeleted();
            assertThat(deleted).isTrue();
        }

        @DisplayName("삭제할 ReadingSpace가 없는 경우, 예외를 던진다.")
        @Test
        void not_found_readingspace_for_update() {
            given(userReadingSpaceRepository.findByReadingSpaceId(UNUSED_READING_SPACE_ID)).willReturn(List.of());

            assertThatThrownBy(() -> readingSpaceService.delete(UNUSED_READING_SPACE_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(READING_SPACE_NOT_FOUND.getMessage());
        }

        @DisplayName("ReadingSpace에 다른 멤버가 존재하는 경우, 예외를 던진다.")
        @Test
        void delete_condition_not_met_1() {
            given(userReadingSpaceRepository.countByReadingSpaceId(READING_SPACE_ID))
                .willReturn(3);

            assertThatThrownBy(() -> readingSpaceService.delete(READING_SPACE_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(MEMBER_IS_STILL_IN_SPACE.getMessage());
        }

        @DisplayName("ReadingSpace에 멤버가 1명이지만 어드민이 아닌 경우, 예외를 던진다.")
        @Test
        void delete_condition_not_met_2() {
            given(userReadingSpaceRepository.findByReadingSpaceId(READING_SPACE_ID))
                .willReturn(USER_READING_MAPPINGS_FOR_DELETE_2);

            assertThatThrownBy(() -> readingSpaceService.delete(READING_SPACE_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(NO_ADMIN_IN_SPACE.getMessage());
        }
    }
}
