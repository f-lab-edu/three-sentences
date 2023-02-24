package com.sh.threesentences.topic.service;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.UNAUTHORIZED_TO_CREATE_TOPIC;

import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.topic.dto.TopicRequestDto;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    private final UserRepository userRepository;

    private final UserReadingSpaceRepository userReadingSpaceRepository;

    public TopicResponseDto save(TopicRequestDto topicRequestDto, String email, Long readingSpaceId) {
        ReadingSpaceMemberRole readingSpaceMemberRole = getReadingSpaceMemberRole(
            readingSpaceId, email);
        checkRoleIsAdmin(readingSpaceMemberRole);

        Topic savedTopic = topicRepository.save(topicRequestDto.toEntity(readingSpaceMemberRole.getReadingSpace()));

        return TopicResponseDto.fromEntity(savedTopic);
    }

    public TopicResponseDto getTopic(Long topicId) {
        Topic topic = topicRepository.findByIdAndDeletedFalse(topicId);
        return TopicResponseDto.fromEntity(topic);
    }

    public List<TopicResponseDto> getTopics(Long readingSpaceId) {
        List<Topic> topics = topicRepository.findByReadingSpaceIdAndDeletedFalse(readingSpaceId);

        return topics.stream()
            .map(TopicResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    public List<TopicResponseDto> getPublicTopics() {
        List<Topic> topics = topicRepository.findAllByDeletedFalse();
        return topics.stream()
            .map(TopicResponseDto::fromEntity)
            .collect(Collectors.toList());
    }

    public void delete(Long topicId, Long readingSpaceId, String email) {
        ReadingSpaceMemberRole readingSpaceMemberRole = getReadingSpaceMemberRole(
            readingSpaceId, email);
        checkRoleIsAdmin(readingSpaceMemberRole);

        Topic topic = topicRepository.findById(topicId)
            .orElseThrow(
                () -> new IllegalStateException(TOPIC_NOT_FOUND.getMessage()));

        topic.delete();
    }

    private ReadingSpaceMemberRole getReadingSpaceMemberRole(Long readingSpaceId, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
        return userReadingSpaceRepository.findByUserAndReadingSpaceId(
                user, readingSpaceId)
            .orElseThrow(
                () -> new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage()));
    }

    private static void checkRoleIsAdmin(ReadingSpaceMemberRole readingSpaceMemberRole) {
        if (!readingSpaceMemberRole.isAdmin()) {
            throw new IllegalStateException(UNAUTHORIZED_TO_CREATE_TOPIC.getMessage());
        }
    }
}
