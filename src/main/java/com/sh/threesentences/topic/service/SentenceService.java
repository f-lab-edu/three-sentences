package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.EXCEED_THE_NUMBER_OF_SENTENCES;
import static com.sh.threesentences.topic.exception.TopicErrorCode.SENTENCE_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.SUBTOPIC_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.UNAUTHORIZED_TO_DELETE_SENTENCE;
import static com.sh.threesentences.users.exception.UserErrorCode.USER_NOT_FOUND;

import com.sh.threesentences.common.exception.ForbiddenException;
import com.sh.threesentences.topic.dto.SentenceRequestDto;
import com.sh.threesentences.topic.dto.SentenceResponseDto;
import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.repository.SentenceRepository;
import com.sh.threesentences.topic.repository.SubTopicRepository;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    private final SubTopicRepository subTopicRepository;

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    private final int MAX_NUMBER_OF_SENTENCE_PER_SUBTOPIC = 3;

    public SentenceResponseDto save(SentenceRequestDto sentenceRequestDto, String email, Long subTopicId) {

        User user = findUserByEmail(email);

        SubTopic subTopic = subTopicRepository.findById(subTopicId)
            .orElseThrow(
                () -> new IllegalStateException(SUBTOPIC_NOT_FOUND.getMessage()));

        checkIfExceedMaxNumberOfSentences(user, subTopic);

        Sentence sentence = sentenceRequestDto.toEntity(subTopic, user.getId());
        Sentence savedSentence = sentenceRepository.save(sentence);

        return SentenceResponseDto.fromEntity(savedSentence);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalStateException(USER_NOT_FOUND.getMessage()));
    }

    public List<SentenceResponseDto> getSentencesBySubtopic(Long subTopicId) {
        SubTopic subTopic = subTopicRepository.findById(subTopicId)
            .orElseThrow(
                () -> new IllegalStateException(SUBTOPIC_NOT_FOUND.getMessage()));
        List<Sentence> sentences = sentenceRepository.findBySubTopic(subTopic);

        return sentences.stream()
            .map(SentenceResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    public List<SentenceResponseDto> getMySentencesBySubTopic(Long subTopicId, String email) {
        User user = findUserByEmail(email);

        SubTopic subTopic = subTopicRepository.findById(subTopicId)
            .orElseThrow(
                () -> new IllegalStateException(SUBTOPIC_NOT_FOUND.getMessage()));
        List<Sentence> sentences = sentenceRepository.findBySubTopic(subTopic);

        return sentences.stream()
            .filter(s -> s.getUserId().equals(user.getId()))
            .map(SentenceResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    private Topic findTopicById(Long topicId) {
        return topicRepository.findById(topicId)
            .orElseThrow(
                () -> new IllegalStateException(TOPIC_NOT_FOUND.getMessage()));
    }

    public List<SentenceResponseDto> getSentencesByTopic(Long topicId) {

        Topic topic = findTopicById(topicId);

        List<SubTopic> subTopics = subTopicRepository.findByTopic(topic);

        return subTopics.stream()
            .map(SubTopic::getSentences)
            .flatMap(List::stream)
            .map(SentenceResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    public List<SentenceResponseDto> getMySentencesByTopic(Long topicId, String email) {

        User user = findUserByEmail(email);

        Topic topic = findTopicById(topicId);

        List<SubTopic> subTopics = subTopicRepository.findByTopic(topic);

        return subTopics.stream()
            .map(SubTopic::getSentences)
            .flatMap(List::stream)
            .filter(s -> s.getUserId().equals(user.getId()))
            .map(SentenceResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    private void checkIfExceedMaxNumberOfSentences(User user, SubTopic subTopic) {
        int sentenceCount = sentenceRepository.countByUserIdAndSubTopic(user.getId(), subTopic);
        if (sentenceCount >= MAX_NUMBER_OF_SENTENCE_PER_SUBTOPIC) {
            throw new IllegalStateException(EXCEED_THE_NUMBER_OF_SENTENCES.getMessage());
        }
    }

    public void deleteSentence(Long sentenceId, String email) {
        User user = findUserByEmail(email);

        Sentence sentence = sentenceRepository.findById(sentenceId)
            .orElseThrow(
                () -> new IllegalStateException(SENTENCE_NOT_FOUND.getMessage()));

        if (isWriterToThisSentence(user, sentence)) {
            throw new ForbiddenException(UNAUTHORIZED_TO_DELETE_SENTENCE.getMessage());
        }

        sentence.delete();
    }

    private static boolean isWriterToThisSentence(User user, Sentence sentence) {
        return !sentence.getUserId().equals(user.getId());
    }
}
