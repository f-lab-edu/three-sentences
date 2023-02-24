package com.sh.threesentences.topic.repository;

import com.sh.threesentences.topic.entity.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Topic findByIdAndDeletedFalse(Long topicId);

    List<Topic> findByReadingSpaceIdAndDeletedFalse(Long readingSpaceId);

    List<Topic> findAllByDeletedFalse();


}
