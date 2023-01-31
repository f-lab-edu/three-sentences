package com.sh.threesentences.readingspace.repository;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingSpaceRepository extends JpaRepository<ReadingSpace, Long> {

    public List<ReadingSpace> findAllByOpenType(OpenType openType);

}
