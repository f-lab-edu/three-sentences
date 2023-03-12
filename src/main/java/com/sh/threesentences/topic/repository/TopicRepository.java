package com.sh.threesentences.topic.repository;

import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.topic.entity.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findById(Long topicId);

    List<Topic> findByReadingSpaceId(Long readingSpaceId);

    Page<Topic> findAllByOpenType(OpenType openType, Pageable pageable);

}
