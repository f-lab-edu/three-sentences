package com.sh.threesentences.readingspace.repository;

import com.sh.threesentences.readingspace.entity.UserReadingSpaceMapping;
import com.sh.threesentences.readingspace.entity.UserReadingSpaceMappingKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadingSpaceRepository extends JpaRepository<UserReadingSpaceMapping, UserReadingSpaceMappingKey> {

}
