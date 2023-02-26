package com.sh.threesentences.topic.repository;

import com.sh.threesentences.topic.entity.Topic;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByIdAndIsDeletedFalse(Long topicId);

    List<Topic> findByReadingSpaceIdAndIsDeletedFalse(Long readingSpaceId);

    List<Topic> findAllByIsDeletedFalseAndOpenTypePublic();


}
