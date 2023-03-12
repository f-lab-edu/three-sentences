package com.sh.threesentences.topic.fixture;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.topic.dto.SentenceRequestDto;
import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.MembershipType;
import java.util.List;

public class SentenceFixture {

    /**
     * User Fixture
     */
    public static Long USER_ID = 1L;

    public static String USER_EMAIL = "abcd@gmail.com";

    public static String USER_NAME = "김길동";

    public static User BASIC_USER = User.builder()
        .id(USER_ID)
        .email(USER_EMAIL)
        .name(USER_NAME)
        .password("12345")
        .membership(MembershipType.BASIC)
        .build();

    public static String USER_EMAIL_WITH_NO_SENTENCE = "no_sentence@gmail.com";

    public static Long USER_ID_WITH_NO_SENTENCE = 10L;

    public static User USER_WITH_NO_SENTENCE = User.builder()
        .id(USER_ID_WITH_NO_SENTENCE)
        .email(USER_EMAIL_WITH_NO_SENTENCE)
        .name("문장 작성 안한 사용자")
        .password("12345")
        .membership(MembershipType.BASIC)
        .build();

    /**
     * ReadingSpace Fixture
     */
    public static ReadingSpace READING_SPACE_1 = ReadingSpace.builder()
        .id(1L)
        .name("test1")
        .description("test description1")
        .openType(OpenType.PUBLIC)
        .profileImageUrl("test url1")
        .build();

    /**
     * Topic Fixture
     */
    public static Topic TOPIC_FOR_SENTENCE = Topic.builder()
        .id(1L)
        .name("리팩토링")
        .description("리팩토링 책입니다.")
        .isbn("12382828")
        .openType(OpenType.PUBLIC)
        .readingSpace(READING_SPACE_1)
        .build();

    public static Long SUBTOPIC_ID = 1L;

    public static Long SUBTOPIC_ID_2 = 2L;

    public static SubTopic SUBTOPIC_FOR_SENTENCE = SubTopic.builder()
        .id(SUBTOPIC_ID)
        .name("서브토픽 1")
        .description("서브토픽 설명 1")
        .startPage(10)
        .endPage(29)
        .topic(TOPIC_FOR_SENTENCE)
        .build();

    public static SubTopic SUBTOPIC_FOR_SENTENCE_2 = SubTopic.builder()
        .id(SUBTOPIC_ID_2)
        .name("서브토픽 2")
        .description("서브토픽 설명 2")
        .startPage(10)
        .endPage(29)
        .topic(TOPIC_FOR_SENTENCE)
        .build();

    public static List<SubTopic> SUBTOPIC_LIST = List.of(SUBTOPIC_FOR_SENTENCE, SUBTOPIC_FOR_SENTENCE_2);


    /**
     * Sentence Fixture
     */
    public static Sentence SENTENCE_1 = Sentence.builder()
        .id(1L)
        .userId(1L)
        .page(20)
        .sentence("클린코드가 무엇인지 알아보자.")
        .thoughts("진짜 궁금했따.")
        .likes(0)
        .subTopic(SUBTOPIC_FOR_SENTENCE)
        .build();

    public static Sentence SENTENCE_2 = Sentence.builder()
        .id(2L)
        .userId(1L)
        .page(25)
        .sentence("가독성이 최고")
        .thoughts("나도 종종 느낀다...")
        .likes(0)
        .subTopic(SUBTOPIC_FOR_SENTENCE)
        .build();

    public static Sentence SENTENCE_3 = Sentence.builder()
        .id(3L)
        .userId(1L)
        .page(30)
        .sentence("클린코드가 꼭 필요할까?")
        .thoughts("일단 서비스가 중요하지")
        .likes(0)
        .subTopic(SUBTOPIC_FOR_SENTENCE)
        .build();

    public static Sentence SENTENCE_4 = Sentence.builder()
        .id(4L)
        .userId(2L)
        .page(22)
        .sentence("몰라")
        .thoughts("몰라")
        .likes(0)
        .subTopic(SUBTOPIC_FOR_SENTENCE)
        .build();

    public static List<Sentence> SENTENCES_LIST_FOR_SUBTOPIC_1 = List.of(
        SENTENCE_1, SENTENCE_2, SENTENCE_3, SENTENCE_4);

    public static List<Sentence> SENTENCES_EMPTY_LIST_FOR_SUBTOPIC_2 = List.of();

    public static SentenceRequestDto SENTENCE_REQUEST_DTO =
        new SentenceRequestDto(14, "이 문장을 쓰고 있다.", "공감이 많이 됨");

    /**
     * Constant Fixture
     */
    public static Long SENTENCE_ID_NOT_EXISTS = 999L;

    public static int VALID_SENTENCE_COUNT_1 = 1;

    public static int INVALID_SENTENCE_COUNT_3 = 3;


}
