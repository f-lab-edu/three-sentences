package com.sh.threesentences.topic.repository;

import com.sh.threesentences.topic.entity.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByIdAndIsDeletedFalse(Long topicId);

    List<Topic> findByReadingSpaceIdAndIsDeletedFalse(Long readingSpaceId);

    List<Topic> findAllByIsDeletedFalse();


}
