package com.sh.threesentences.topic.service;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;

import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.topic.dto.TopicRequestDto;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.entity.Topic;
import com.sh.threesentences.topic.repository.TopicRepository;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
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

        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
        ReadingSpaceMemberRole readingSpaceMemberRole = userReadingSpaceRepository.findByUserAndReadingSpaceId(
                user, readingSpaceId)
            .orElseThrow(
                () -> new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage()));

        if (!readingSpaceMemberRole.isAdmin()) {
            throw new IllegalStateException("스페이스 관리자만 토픽을 생성할 수 있습니다.");
        }

        Topic savedTopic = topicRepository.save(topicRequestDto.toEntity(readingSpaceMemberRole.getReadingSpace()));

        return TopicResponseDto.fromEntity(savedTopic);
    }
}
