package com.sh.threesentences.readingspace.fixture;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.MembershipType;
import java.util.List;

public class ReadingSpaceFixture {

    public static Long USER_ID = 1L;

    public static String USER_NAME = "김길동";

    public static Long READING_SPACE_ID = 1L;

    public static Long UNUSED_READING_SPACE_ID = 9999L;

    public static String NAME = "책을 읽자";

    public static String DESCRIPTION = "책을 읽고 생각을공유하는 모임입니다. ";

    public static OpenType OPENTYPE = OpenType.PUBLIC;

    public static String profileURL = null;

    public static ReadingSpaceRequestDto VALID_READING_SPACE_REQUEST = new ReadingSpaceRequestDto(
        NAME, DESCRIPTION, OPENTYPE, profileURL);

    public static ReadingSpaceResponseDto VALID_READING_SPACE_RESPONSE = new ReadingSpaceResponseDto(READING_SPACE_ID,
        NAME, DESCRIPTION, OPENTYPE, profileURL);

    public static User USER_1 = User.builder()
        .id(USER_ID)
        .name(USER_NAME)
        .password("12345")
        .membership(MembershipType.BASIC)
        .build();

    public static User USER_2 = User.builder()
        .id(2L)
        .name("test2")
        .password("12345")
        .membership(MembershipType.BASIC)
        .build();

    public static ReadingSpace READING_SPACE_1 = ReadingSpace.builder()
        .id(1L)
        .name("test1")
        .description("test description1")
        .openType(OpenType.PUBLIC)
        .profileImageUrl("test url1")
        .build();

    public static ReadingSpace READING_SPACE_2 = ReadingSpace.builder()
        .id(2L)
        .name("test2")
        .description("test description2")
        .openType(OpenType.PUBLIC)
        .profileImageUrl("test url2")
        .build();

    public static ReadingSpace READING_SPACE_3 = ReadingSpace.builder()
        .id(3L)
        .name("test3")
        .description("test description3")
        .openType(OpenType.PRIVATE)
        .profileImageUrl("test url3")
        .build();

    public static ReadingSpace READING_SPACE_4 = ReadingSpace.builder()
        .id(4L)
        .name("test4")
        .description("test description4")
        .openType(OpenType.PUBLIC)
        .profileImageUrl("test url4")
        .build();

    public static List<ReadingSpace> ALL_READING_SPACES = List.of(
        READING_SPACE_1,
        READING_SPACE_2,
        READING_SPACE_3,
        READING_SPACE_4
    );

    public static int PUBLIC_READING_SPACES_SIZE = (int) ALL_READING_SPACES
        .stream()
        .filter(s -> s.getOpenType().equals(OpenType.PUBLIC)).count();

    public static List<ReadingSpaceMemberRole> USER_READING_MAPPINGS = List.of(
        new ReadingSpaceMemberRole(USER_1, READING_SPACE_1, UserRole.ADMIN),
        new ReadingSpaceMemberRole(USER_1, READING_SPACE_2, UserRole.REDINGMATE),
        new ReadingSpaceMemberRole(USER_1, READING_SPACE_3, UserRole.REDINGMATE)
    );

    public static int MY_READING_SPACES_SIZE = (int) USER_READING_MAPPINGS
        .stream()
        .filter(s -> s.getUser().getId().equals(USER_ID)).count();

    public static List<ReadingSpaceMemberRole> USER_READING_MAPPINGS_FOR_DELETE_CONDITION_MET = List.of(
        new ReadingSpaceMemberRole(USER_1, READING_SPACE_1, UserRole.ADMIN)
    );


    public static List<ReadingSpaceMemberRole> USER_READING_MAPPINGS_FOR_DELETE_1 = List.of(
        new ReadingSpaceMemberRole(USER_1, READING_SPACE_1, UserRole.ADMIN),
        new ReadingSpaceMemberRole(USER_2, READING_SPACE_1, UserRole.REDINGMATE)
    );

    public static List<ReadingSpaceMemberRole> USER_READING_MAPPINGS_FOR_DELETE_2 = List.of(
        new ReadingSpaceMemberRole(USER_1, READING_SPACE_1, UserRole.REDINGMATE)
    );
}
