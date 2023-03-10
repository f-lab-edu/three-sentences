package com.sh.threesentences.topic.service;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.repository.ReadingSpaceRepository;
import com.sh.threesentences.topic.dto.TopicRequestDto;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.service.AuthorityService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    private final ReadingSpaceRepository readingSpaceRepository;

    private final AuthorityService authorityService;

    public TopicResponseDto save(TopicRequestDto topicRequestDto, String email, Long readingSpaceId) {

        authorityService.checkUserIsAdminInReadingSpace(readingSpaceId, email);

        ReadingSpace readingSpace = readingSpaceRepository.findById(readingSpaceId)
            .orElseThrow(() -> new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage()));

        Topic savedTopic = topicRepository.save(topicRequestDto.toEntity(readingSpace));

        return TopicResponseDto.fromEntity(savedTopic);
    }

    public TopicResponseDto getTopic(Long topicId) {
        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(
                () -> new IllegalStateException(TOPIC_NOT_FOUND.getMessage()));
        return TopicResponseDto.fromEntity(topic);
    }

    public List<TopicResponseDto> getTopics(Long readingSpaceId) {
        List<Topic> topics = topicRepository.findByReadingSpaceId(readingSpaceId);
        return convertTopicToTopicResponseDto(topics);
    }

    public List<TopicResponseDto> getPublicTopics(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        Page<Topic> pagingTopics = topicRepository.findAllByOpenType(OpenType.PUBLIC, pageRequest);
        return convertTopicToTopicResponseDto(pagingTopics.getContent());
    }

    public void delete(Long topicId, Long readingSpaceId, String email) {
        authorityService.checkUserIsAdminInReadingSpace(readingSpaceId, email);

        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(
                () -> new IllegalStateException(TOPIC_NOT_FOUND.getMessage()));

        topic.delete();

        List<SubTopic> subTopics = topic.getSubTopics();
        subTopics.forEach(SubTopic::delete);

    }

    private static List<TopicResponseDto> convertTopicToTopicResponseDto(List<Topic> topics) {
        return topics.stream()
            .map(TopicResponseDto::fromEntity)
            .collect(Collectors.toList());
    }
}
