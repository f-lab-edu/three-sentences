package com.sh.threesentences.topic.fixture;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.topic.dto.SubTopicRequestDto;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.MembershipType;

public class SubTopicFixture {


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

    public static Long NOT_FOUND_SUBTOPIC_ID = 9999L;

    public static Long SUBTOPIC_ID = 1L;

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

    public static SubTopicRequestDto SUBTOPIC_REQUEST_DTO = new SubTopicRequestDto("서브토픽1", "서브토픽1 테스트입니다.", 1, 10);

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

}
