package com.sh.threesentences.users.service;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;
import static com.sh.threesentences.topic.exception.TopicErrorCode.UNAUTHORIZED_TO_CREATE_TOPIC;

import com.sh.threesentences.common.exception.ForbiddenException;
import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.repository.UserReadingSpaceRepository;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.exception.UserNotFoundException;
import com.sh.threesentences.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityService {

    private final UserRepository userRepository;

    private final UserReadingSpaceRepository userReadingSpaceRepository;

    public ReadingSpaceMemberRole getReadingSpaceMemberRole(Long readingSpaceId, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);

        return userReadingSpaceRepository.findByUserAndReadingSpaceId(user, readingSpaceId)
            .orElseThrow(
                () -> new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage()));
    }

    public void checkRoleIsAdmin(ReadingSpaceMemberRole readingSpaceMemberRole) {
        if (!readingSpaceMemberRole.isAdmin()) {
            throw new IllegalStateException(UNAUTHORIZED_TO_CREATE_TOPIC.getMessage());
        }
    }

    public void checkUserIsAdminInReadingSpace(Long readingSpaceId, String email) {
        checkRoleIsAdmin(this.getReadingSpaceMemberRole(readingSpaceId, email));
    }
}
