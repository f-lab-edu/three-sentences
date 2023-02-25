package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.EXCEED_THE_NUMBER_OF_SENTENCES;
import static com.sh.threesentences.topic.exception.TopicErrorCode.SUBTOPIC_NOT_FOUND;
import static com.sh.threesentences.users.exception.UserErrorCode.USER_NOT_FOUND;

import com.sh.threesentences.topic.dto.SentenceRequestDto;
import com.sh.threesentences.topic.dto.SentenceResponseDto;
import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.repository.SentenceRepository;
import com.sh.threesentences.topic.repository.SubTopicRepository;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SentenceService {

    private final SentenceRepository sentenceRepository;

    private final SubTopicRepository subTopicRepository;

    private final UserRepository userRepository;

    public SentenceResponseDto save(SentenceRequestDto sentenceRequestDto, String email, Long subTopicId) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalStateException(USER_NOT_FOUND.getMessage()));

        SubTopic subTopic = subTopicRepository.findById(subTopicId)
            .orElseThrow(
                () -> new IllegalStateException(SUBTOPIC_NOT_FOUND.getMessage()));

        checkIfExceedMaxNumberOfSentences(user, subTopic);

        Sentence sentence = sentenceRequestDto.toEntity(subTopic, user.getId());
        Sentence savedSentence = sentenceRepository.save(sentence);

        return SentenceResponseDto.fromEntity(savedSentence);
    }

    private void checkIfExceedMaxNumberOfSentences(User user, SubTopic subTopic) {
        int sentenceCount = sentenceRepository.countByUserIdAndSubTopic(user.getId(), subTopic);
        if (sentenceCount >= 3) {
            throw new IllegalStateException(EXCEED_THE_NUMBER_OF_SENTENCES.getMessage());
        }
    }
}
