package com.sh.threesentences.topic.fixture;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.topic.dto.TopicRequestDto;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.MembershipType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TopicFixture {

    public static Long USER_ID = 1L;

    public static String USER_EMAIL = "abcd@gmail.com";

    public static String USER_NAME = "김길동";

    public static Long READING_SPACE_ID = 1L;

    public static Long TOPIC_ID = 1L;

    public static Long NOT_FOUND_TOPIC_ID = 123123L;

    public static String TOPIC_NAME = "test_topic";

    public static String TOPIC_DESCRIPTION = "this is for test.";

    public static String TOPIC_ISBN = "1234567890";

    public static OpenType TOPIC_OPEN_TYPE = OpenType.PUBLIC;

    public static User BASIC_USER = User.builder()
        .id(USER_ID)
        .email(USER_EMAIL)
        .name(USER_NAME)
        .password("12345")
        .membership(MembershipType.BASIC)
        .build();

    public static ReadingSpace BASIC_READING_SPACE = ReadingSpace.builder()
        .id(READING_SPACE_ID)
        .name("test1")
        .description("test description1")
        .openType(OpenType.PUBLIC)
        .profileImageUrl("test url1")
        .build();

    public static ReadingSpaceMemberRole ADMIN_USER_READINGSPACE_MEMBER_ROLE =
        new ReadingSpaceMemberRole(BASIC_USER, BASIC_READING_SPACE, UserRole.ADMIN);

    public static ReadingSpaceMemberRole BASIC_USER_READINGSPACE_MEMBER_ROLE =
        new ReadingSpaceMemberRole(BASIC_USER, BASIC_READING_SPACE, UserRole.READINGMATE);

    public static TopicRequestDto TOPIC_REQUEST_DTO = new TopicRequestDto(TOPIC_NAME,
        TOPIC_DESCRIPTION, TOPIC_ISBN, TOPIC_OPEN_TYPE);

    public static TopicRequestDto createTopicWithInvalidName(String invalidName) {
        return new TopicRequestDto(invalidName,
            TOPIC_DESCRIPTION, TOPIC_ISBN, TOPIC_OPEN_TYPE);
    }


    public static TopicResponseDto TOPIC_RESPONSE_DTO = new TopicResponseDto(TOPIC_ID, TOPIC_NAME,
        TOPIC_DESCRIPTION, TOPIC_ISBN, TOPIC_OPEN_TYPE, new ArrayList<>());

    public static Topic TOPIC = Topic.builder()
        .id(TOPIC_ID)
        .name(TOPIC_NAME)
        .description(TOPIC_DESCRIPTION)
        .isbn(TOPIC_ISBN)
        .openType(TOPIC_OPEN_TYPE)
        .readingSpace(BASIC_READING_SPACE)
        .build();

    public static SubTopic SUBTOPIC_1 = SubTopic.builder()
        .id(1L)
        .name("서브토픽 1")
        .description("서브토픽 설명 1")
        .startPage(10)
        .endPage(29)
        .topic(TOPIC)
        .build();

    public static SubTopic SUBTOPIC_2 = SubTopic.builder()
        .id(2L)
        .name("서브토픽 2")
        .description("서브토픽 설명 2")
        .startPage(30)
        .endPage(50)
        .topic(TOPIC)
        .build();

    public static Topic TOPIC_2 = Topic.builder()
        .id(2L)
        .name("TOPIC_2")
        .description("TOPIC_DESCRIPTION_2")
        .isbn("TOPIC_NAVER_BOOK_ID_2")
        .openType(OpenType.PUBLIC)
        .readingSpace(BASIC_READING_SPACE)
        .build();

    public static Topic TOPIC_3 = Topic.builder()
        .id(3L)
        .name("TOPIC_3")
        .description("TOPIC_DESCRIPTION_3")
        .isbn("TOPIC_NAVER_BOOK_ID_3")
        .openType(OpenType.PUBLIC)
        .readingSpace(BASIC_READING_SPACE)
        .build();

    public static Topic TOPIC_4 = Topic.builder()
        .id(4L)
        .name("TOPIC_4")
        .description("TOPIC_DESCRIPTION_4")
        .isbn("TOPIC_NAVER_BOOK_ID_4")
        .openType(OpenType.PRIVATE)
        .readingSpace(BASIC_READING_SPACE)
        .build();

    public static List<TopicResponseDto> TOPIC_RESPONSE_RESULTS = List.of(TOPIC, TOPIC_2, TOPIC_3, TOPIC_4).stream()
        .map(TopicResponseDto::fromEntity)
        .collect(Collectors.toList());
}
