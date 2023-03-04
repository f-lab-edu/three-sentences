package com.sh.threesentences.topic.service;

import static com.sh.threesentences.topic.exception.TopicErrorCode.SUBTOPIC_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

import com.sh.threesentences.topic.dto.SubTopicRequestDto;
import com.sh.threesentences.topic.dto.SubTopicResponseDto;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.repository.SubTopicRepository;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubTopicService {

    private final SubTopicRepository subTopicRepository;

    private final TopicRepository topicRepository;

    private final AuthorityService authorityService;

    public SubTopicResponseDto save(SubTopicRequestDto subTopicRequestDto, String email, Long readingSpaceId,
        Long topicId) {

        authorityService.checkUserIsAdminInReadingSpace(readingSpaceId, email);

        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(
                () -> new IllegalStateException(TOPIC_NOT_FOUND.getMessage()));

        topic.checkDelete();

        SubTopic subTopic = subTopicRequestDto.toEntity(topic);
        SubTopic savedTopic = subTopicRepository.save(subTopic);

        return SubTopicResponseDto.fromEntity(savedTopic);
    }

    public void delete(Long readingSpaceId, Long subtopicId, String email) {
        authorityService.checkUserIsAdminInReadingSpace(readingSpaceId, email);

        SubTopic subTopic = subTopicRepository.findById(subtopicId)
            .orElseThrow(
                () -> new IllegalStateException(SUBTOPIC_NOT_FOUND.getMessage()));

        subTopic.delete();
    }


}
