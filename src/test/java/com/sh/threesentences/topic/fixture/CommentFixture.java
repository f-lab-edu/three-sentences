package com.sh.threesentences.topic.fixture;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.topic.dto.CommentRequestDto;
import com.sh.threesentences.topic.entity.Comment;
import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;

public class CommentFixture {


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

    public static SubTopic SUBTOPIC_FOR_SENTENCE = SubTopic.builder()
        .id(1L)
        .name("서브토픽 1")
        .description("서브토픽 설명 1")
        .startPage(10)
        .endPage(29)
        .topic(TOPIC_FOR_SENTENCE)
        .build();

    public static Sentence SENTENCE_1 = Sentence.builder()
        .id(1L)
        .userId(1L)
        .page(20)
        .sentence("클린코드가 무엇인지 알아보자.")
        .thoughts("진짜 궁금했따.")
        .likes(0)
        .subTopic(SUBTOPIC_FOR_SENTENCE)
        .build();

    public static Long COMMENT_1_ID = 1L;

    public static Comment COMMENT_1 = Comment.builder()
        .userId(1L)
        .contents("재밌는 감상평이었네요.")
        .likes(0)
        .sentence(SENTENCE_1)
        .build();

    public static CommentRequestDto COMMENT_REQUEST_DTO =
        new CommentRequestDto(1L, 1L, "재밌는 감상평이었네요.");

    public static Long SENTENCE_ID_NOT_EXISTS = 999L;
    public static CommentRequestDto COMMENT_REQUEST_DTO_WITH_NOT_EXISTS_SENTENCE =
        new CommentRequestDto(SENTENCE_ID_NOT_EXISTS, 1L, "재밌는 감상평이었네요.");

}
