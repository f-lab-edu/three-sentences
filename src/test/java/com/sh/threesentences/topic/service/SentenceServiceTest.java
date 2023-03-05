package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.EXCEED_THE_NUMBER_OF_SENTENCES;
import static com.sh.threesentences.topic.exception.TopicErrorCode.SENTENCE_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.UNAUTHORIZED_TO_DELETE_SENTENCE;
import static com.sh.threesentences.topic.fixture.SentenceFixture.BASIC_USER;
import static com.sh.threesentences.topic.fixture.SentenceFixture.INVALID_SENTENCE_COUNT_3;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SENTENCES_EMPTY_LIST_FOR_SUBTOPIC_2;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SENTENCES_LIST_FOR_SUBTOPIC_1;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SENTENCE_1;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SENTENCE_ID_NOT_EXISTS;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SENTENCE_REQUEST_DTO;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SUBTOPIC_FOR_SENTENCE;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SUBTOPIC_FOR_SENTENCE_2;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SUBTOPIC_ID;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SUBTOPIC_ID_2;
import static com.sh.threesentences.topic.fixture.SentenceFixture.SUBTOPIC_LIST;
import static com.sh.threesentences.topic.fixture.SentenceFixture.TOPIC_FOR_SENTENCE;
import static com.sh.threesentences.topic.fixture.SentenceFixture.USER_EMAIL;
import static com.sh.threesentences.topic.fixture.SentenceFixture.USER_EMAIL_WITH_NO_SENTENCE;
import static com.sh.threesentences.topic.fixture.SentenceFixture.USER_ID;
import static com.sh.threesentences.topic.fixture.SentenceFixture.USER_WITH_NO_SENTENCE;
import static com.sh.threesentences.topic.fixture.SentenceFixture.VALID_SENTENCE_COUNT_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sh.threesentences.common.exception.ForbiddenException;
import com.sh.threesentences.topic.dto.SentenceResponseDto;
import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.repository.SentenceRepository;
import com.sh.threesentences.topic.repository.SubTopicRepository;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("* SubTopicService")
@ExtendWith(MockitoExtension.class)
class SentenceServiceTest {

    @Mock
    private SentenceRepository sentenceRepository;

    @Mock
    private SubTopicRepository subTopicRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SentenceService sentenceService;

    @Nested
    @DisplayName("** save method")
    class ContextSaveMethod {

        @DisplayName("└ 문장을 등록 후 리턴한다.")
        @Test
        void createTopic() {

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(BASIC_USER));

            given(subTopicRepository.findById(SUBTOPIC_ID)).willReturn(Optional.of(SUBTOPIC_FOR_SENTENCE));

            given(sentenceRepository.countByUserIdAndSubTopic(USER_ID, SUBTOPIC_FOR_SENTENCE)).willReturn(
                VALID_SENTENCE_COUNT_1);

            given(sentenceRepository.save(any(Sentence.class))).will(invocation -> {
                Sentence sentence = invocation.getArgument(0);
                return Sentence.builder()
                    .sentence(sentence.getSentence())
                    .thoughts(sentence.getThoughts())
                    .page(sentence.getPage())
                    .subTopic(sentence.getSubTopic())
                    .build();
            });

            SentenceResponseDto savedSentence = sentenceService.save(SENTENCE_REQUEST_DTO, USER_EMAIL, SUBTOPIC_ID);

            assertThat(savedSentence.getPage()).isEqualTo(SENTENCE_REQUEST_DTO.getPage());
            assertThat(savedSentence.getSentence()).isEqualTo(SENTENCE_REQUEST_DTO.getSentence());
            assertThat(savedSentence.getThoughts()).isEqualTo(SENTENCE_REQUEST_DTO.getThoughts());
        }

        @DisplayName("└ 1개의 서브토픽에 등록된 문장이 3개 이상이면 예외를 던진다.")
        @Test
        void createTopicWhenSentenceCountOverThree() {

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(BASIC_USER));

            given(subTopicRepository.findById(SUBTOPIC_ID)).willReturn(Optional.of(SUBTOPIC_FOR_SENTENCE));

            given(sentenceRepository.countByUserIdAndSubTopic(USER_ID, SUBTOPIC_FOR_SENTENCE)).willReturn(
                INVALID_SENTENCE_COUNT_3);

            assertThatThrownBy(() -> sentenceService.save(SENTENCE_REQUEST_DTO, USER_EMAIL, SUBTOPIC_ID))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(EXCEED_THE_NUMBER_OF_SENTENCES.getMessage());
        }

    }

    @Nested
    @DisplayName("** getSentencesBySubtopic method")
    class ContextGetSentencesBySubtopicMethod {

        @DisplayName("└ 서브토픽에 등록된 문장을 조회한다.")
        @Test
        void getSentencesBySubtopic() {

            given(subTopicRepository.findById(SUBTOPIC_ID)).willReturn(Optional.of(SUBTOPIC_FOR_SENTENCE));
            given(sentenceRepository.findBySubTopic(SUBTOPIC_FOR_SENTENCE)).willReturn(SENTENCES_LIST_FOR_SUBTOPIC_1);

            List<SentenceResponseDto> sentencesBySubtopic = sentenceService.getSentencesBySubtopic(SUBTOPIC_ID);

            assertThat(sentencesBySubtopic.size()).isEqualTo(SENTENCES_LIST_FOR_SUBTOPIC_1.size());
        }

        @DisplayName("└ 서브토픽에 등록된 문장이 없다면 빈 리스트를 조회하게 된다.")
        @Test
        void getSentencesBySubtopicWhenNoSentences() {

            given(subTopicRepository.findById(SUBTOPIC_ID_2)).willReturn(Optional.of(SUBTOPIC_FOR_SENTENCE_2));
            given(sentenceRepository.findBySubTopic(SUBTOPIC_FOR_SENTENCE_2)).willReturn(
                SENTENCES_EMPTY_LIST_FOR_SUBTOPIC_2);

            List<SentenceResponseDto> sentencesBySubtopic = sentenceService.getSentencesBySubtopic(SUBTOPIC_ID_2);

            assertThat(sentencesBySubtopic.size()).isZero();
        }
    }


    @Nested
    @DisplayName("** getMySentencesBySubTopic method")
    class ContextGetMySentencesBySubTopicMethod {


        @DisplayName("└ 사용자가 서브토픽에 등록한 문장을 조회한다.")
        @Test
        void getMySentencesBySubtopic() {
            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(BASIC_USER));
            given(subTopicRepository.findById(SUBTOPIC_ID)).willReturn(Optional.of(SUBTOPIC_FOR_SENTENCE));
            given(sentenceRepository.findBySubTopic(SUBTOPIC_FOR_SENTENCE)).willReturn(
                SENTENCES_LIST_FOR_SUBTOPIC_1);

            List<SentenceResponseDto> mySentencesBySubtopic = sentenceService.getMySentencesBySubTopic(SUBTOPIC_ID,
                USER_EMAIL);

            assertThat(mySentencesBySubtopic.size()).isEqualTo(SENTENCES_LIST_FOR_SUBTOPIC_1.size() - 1);
        }

        @DisplayName("└ 사용자가 서브토픽에 등록된 문장이 없다면 빈 리스트를 조회하게 된다.")
        @Test
        void getMySentencesBySubtopicWhenNoSentences() {
            given(userRepository.findByEmail(USER_EMAIL_WITH_NO_SENTENCE)).willReturn(
                Optional.of(USER_WITH_NO_SENTENCE));
            given(subTopicRepository.findById(SUBTOPIC_ID)).willReturn(Optional.of(SUBTOPIC_FOR_SENTENCE));
            given(sentenceRepository.findBySubTopic(SUBTOPIC_FOR_SENTENCE)).willReturn(
                SENTENCES_LIST_FOR_SUBTOPIC_1);

            List<SentenceResponseDto> mySentencesBySubtopic = sentenceService.getMySentencesBySubTopic(SUBTOPIC_ID,
                USER_EMAIL_WITH_NO_SENTENCE);

            assertThat(mySentencesBySubtopic.size()).isZero();
        }
    }


    @Nested
    @DisplayName("** getSentencesByTopic method")
    class ContextGetSentencesByTopicMethod {

        @DisplayName("└ 토픽에 등록된 전체 문장을 조회한다.")
        @Test
        void getSentencesByTopic() {

            given(topicRepository.findById(TOPIC_FOR_SENTENCE.getId())).willReturn(Optional.of(TOPIC_FOR_SENTENCE));
            given(subTopicRepository.findByTopic(TOPIC_FOR_SENTENCE)).willReturn(SUBTOPIC_LIST);

            List<SentenceResponseDto> sentencesByTopic = sentenceService.getSentencesByTopic(
                TOPIC_FOR_SENTENCE.getId());

            List<Sentence> mySentences = SUBTOPIC_LIST.stream()
                .map(SubTopic::getSentences)
                .flatMap(List::stream)
                .collect(Collectors.toList());

            assertThat(sentencesByTopic.size()).isEqualTo(mySentences.size());
        }
    }

    @Nested
    @DisplayName("** getMySentencesByTopic method")
    class ContextGetMySentencesByTopicMethod {

        @DisplayName("└ 사용자가 토픽에 등록한 모든 문장을 조회한다.")
        @Test
        void getMySentencesByTopic() {
            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(BASIC_USER));
            given(topicRepository.findById(TOPIC_FOR_SENTENCE.getId())).willReturn(Optional.of(TOPIC_FOR_SENTENCE));
            given(subTopicRepository.findByTopic(TOPIC_FOR_SENTENCE)).willReturn(SUBTOPIC_LIST);

            List<SentenceResponseDto> sentencesByTopic = sentenceService.getMySentencesByTopic(
                TOPIC_FOR_SENTENCE.getId(), USER_EMAIL);

            List<Sentence> mySentences = SUBTOPIC_LIST.stream()
                .map(SubTopic::getSentences)
                .flatMap(List::stream)
                .filter(s -> s.getUserId().equals(BASIC_USER.getId()))
                .collect(Collectors.toList());

            assertThat(sentencesByTopic.size()).isEqualTo(mySentences.size());
        }
    }

    @Nested
    @DisplayName("** deleteSentence method")
    class ContextDeleteSentenceMethod {

        @DisplayName("└ 선택한 문장을 삭제한다.")
        @Test
        void deleteSentences() {

            assertFalse(SENTENCE_1.getIsDeleted());

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(BASIC_USER));
            given(sentenceRepository.findById(SENTENCE_1.getId())).willReturn(Optional.of(SENTENCE_1));

            sentenceService.deleteSentence(SENTENCE_1.getId(), USER_EMAIL);

            assertTrue(SENTENCE_1.getIsDeleted());
        }

        @DisplayName("└ 존재하지 않는 문장을 삭제하려하면 예외를 던진다.")
        @Test
        void deleteNotFoundSentence() {

            given(userRepository.findByEmail(USER_EMAIL)).willReturn(Optional.of(BASIC_USER));
            given(sentenceRepository.findById(SENTENCE_ID_NOT_EXISTS)).willReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> sentenceService.deleteSentence(SENTENCE_ID_NOT_EXISTS, USER_EMAIL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(SENTENCE_NOT_FOUND.getMessage());
        }

        @DisplayName("└ 문장을 작성한 사용자가 삭제를 시도할 때 예외를 던진다.")
        @Test
        void unAuthorizedToDeleteSentence() {
            given(userRepository.findByEmail(USER_EMAIL_WITH_NO_SENTENCE)).willReturn(
                Optional.of(USER_WITH_NO_SENTENCE));
            given(sentenceRepository.findById(SENTENCE_1.getId())).willReturn(Optional.of(SENTENCE_1));

            Assertions.assertThatThrownBy(
                    () -> sentenceService.deleteSentence(SENTENCE_1.getId(), USER_EMAIL_WITH_NO_SENTENCE))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage(UNAUTHORIZED_TO_DELETE_SENTENCE.getMessage());
        }
    }

}
