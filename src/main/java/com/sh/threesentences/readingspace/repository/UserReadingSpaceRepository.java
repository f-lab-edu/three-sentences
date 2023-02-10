package com.sh.threesentences.readingspace.repository;

import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.entity.UserReadingSpaceMappingId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingSpaceRepository extends JpaRepository<ReadingSpaceMemberRole, UserReadingSpaceMappingId> {

    List<ReadingSpaceMemberRole> findByUserId(Long id);

    List<ReadingSpaceMemberRole> findByReadingSpaceId(Long id);

    int countByReadingSpaceId(Long id);

}
