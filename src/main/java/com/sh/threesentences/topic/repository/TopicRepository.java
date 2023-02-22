package com.sh.threesentences.topic.repository;

import com.sh.threesentences.topic.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}
