package com.sh.threesentences.topic.repository;

import com.sh.threesentences.topic.entity.SubTopic;
import com.sh.threesentences.topic.entity.Topic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTopicRepository extends JpaRepository<SubTopic, Long> {

    List<SubTopic> findByTopic(Topic topic);
}
