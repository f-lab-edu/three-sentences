package com.sh.threesentences.readingspace.service;


import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.DELETE_ADMIN_ONLY;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.MEMBER_IS_STILL_IN_SPACE;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.ALL_READING_SPACES;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.MY_READING_SPACES_SIZE;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.PUBLIC_READING_SPACES_SIZE;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.READING_SPACE_ID;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.UNUSED_READING_SPACE_ID;
import static com.sh.threesentences.readingspace.fixture.ReadingSpaceFixture.USER_1;
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
import com.sh.threesentences.users.repository.UserRepository;
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
@DisplayName("* ReadingSpaceService")
class ReadingSpaceServiceTest {

    @Mock
    private ReadingSpaceRepository readingSpaceRepository;

    @Mock
    UserReadingSpaceRepository userReadingSpaceRepository;

    @Mock
    UserRepository userRepository;

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
    @DisplayName("** create method")
    class ContextCreatedMethod {


        @DisplayName("??? ReadingSpace??? ?????? ??? ????????????.")
        @Test
        void createReadingSpace() {

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.ofNullable(USER_1));

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

            ReadingSpaceResponseDto readingSpaceResponseDto = readingSpaceService.create(
                validReadingSpaceRequestDto, USER_EMAIL);

            assertThat(readingSpaceResponseDto.getName()).isEqualTo(validReadingSpaceResponseDto.getName());
            assertThat(readingSpaceResponseDto.getDescription()).isEqualTo(
                validReadingSpaceResponseDto.getDescription());
            assertThat(readingSpaceResponseDto.getOpenType()).isEqualTo(validReadingSpaceResponseDto.getOpenType());
            assertThat(readingSpaceResponseDto.getProfileImageUrl()).isEqualTo(
                validReadingSpaceResponseDto.getProfileImageUrl());
        }
    }


    @Nested
    @DisplayName("** getPublicReadingSpaces method")
    class ContextGetPublicReadingSpacesMethod {

        @DisplayName("??? ????????? ?????? ReadingSpace??? ????????????.")
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
    @DisplayName("** update method")
    class ContextUpdateMethod {

        @DisplayName("??? ReadingSpace ????????? ???????????? ??????.")
        @Test
        void update_readingspace() {
            // JPA??? ?????? ????????? ?????? ??????????????? ?????????,
            // ???????????? ??? ?????????????????? ???????????? ?????? ??????????????? ?????? ???????????? ????????? ??????????
        }

        @DisplayName("??? ??????????????? ReadingSpace??? ?????? ??????, ????????? ?????????.")
        @Test
        void not_found_readingspace_for_update() {
            given(readingSpaceRepository.findById(UNUSED_READING_SPACE_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> readingSpaceService.update(VALID_READING_SPACE_REQUEST, UNUSED_READING_SPACE_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(READING_SPACE_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("** getMyReadingSpaces method")
    class ContextGetMyReadingSpaceMethod {

        @DisplayName("??? ?????? ???????????? readingspace??? ????????????.")
        @Test
        void getMyReadingSpaces() {

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.ofNullable(USER_1));

            given(userReadingSpaceRepository.findByUserId(USER_ID)).willReturn(
                USER_READING_MAPPINGS
            );

            List<ReadingSpaceResponseDto> myReadingSpaces = readingSpaceService.getMyReadingSpaces(USER_EMAIL);

            assertThat(myReadingSpaces).hasSize(MY_READING_SPACES_SIZE);
        }
    }

    @Nested
    @DisplayName("** delete method")
    class ContextDeleteMethod {

        @DisplayName("??? ReadingSpace??? ????????????.")
        @Test
        void delete_reading_space() {

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.ofNullable(USER_1));

            given(userReadingSpaceRepository.findByUserAndReadingSpaceId(USER_1, READING_SPACE_ID)).willReturn(
                Optional.ofNullable(USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET));

            given(userReadingSpaceRepository.findByReadingSpaceId(READING_SPACE_ID)).willReturn(
                List.of(USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET));

            given(userReadingSpaceRepository.countByReadingSpaceId(READING_SPACE_ID))
                .willReturn(1);

            readingSpaceService.delete(READING_SPACE_ID, USER_EMAIL);
            boolean deleted = USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET.getReadingSpace().getIsDeleted();
            assertThat(deleted).isTrue();
        }

        @DisplayName("??? ????????? ReadingSpace??? ?????? ??????, ????????? ?????????.")
        @Test
        void not_found_readingspace_for_update() {
            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.ofNullable(USER_1));

            given(userReadingSpaceRepository.findByUserAndReadingSpaceId(USER_1, UNUSED_READING_SPACE_ID)).willReturn(
                Optional.empty());

            assertThatThrownBy(() -> readingSpaceService.delete(UNUSED_READING_SPACE_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(READING_SPACE_NOT_FOUND.getMessage());
        }

        @DisplayName("??? ReadingSpace??? ?????? ????????? ???????????? ??????, ????????? ?????????.")
        @Test
        void delete_condition_not_met_1() {

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.ofNullable(USER_1));

            given(userReadingSpaceRepository.findByUserAndReadingSpaceId(USER_1, READING_SPACE_ID)).willReturn(
                Optional.ofNullable(USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET));

            given(userReadingSpaceRepository.countByReadingSpaceId(READING_SPACE_ID))
                .willReturn(3);

            assertThatThrownBy(() -> readingSpaceService.delete(READING_SPACE_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(MEMBER_IS_STILL_IN_SPACE.getMessage());
        }

        @DisplayName("??? ReadingSpace??? ????????? 1???????????? ???????????? ?????? ??????, ????????? ?????????.")
        @Test
        void delete_condition_not_met_2() {
            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.ofNullable(USER_1));

            given(userReadingSpaceRepository.findByUserAndReadingSpaceId(USER_1, READING_SPACE_ID)).willReturn(
                Optional.ofNullable(USER_READING_MAPPINGS_FOR_DELETE_2));

            assertThatThrownBy(() -> readingSpaceService.delete(READING_SPACE_ID, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(DELETE_ADMIN_ONLY.getMessage());

        }
    }
}
