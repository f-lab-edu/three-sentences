package com.sh.threesentences.readingspace.repository;

import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.entity.UserReadingSpaceMappingId;
import com.sh.threesentences.users.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingSpaceRepository extends JpaRepository<ReadingSpaceMemberRole, UserReadingSpaceMappingId> {

    List<ReadingSpaceMemberRole> findByUserId(Long id);

    List<ReadingSpaceMemberRole> findByReadingSpaceId(Long id);

    int countByReadingSpaceId(Long id);

    Optional<ReadingSpaceMemberRole> findByUserAndReadingSpaceId(User user, Long id);

}
