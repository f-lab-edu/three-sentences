package com.sh.threesentences.topic.repository;

import com.sh.threesentences.topic.entity.Sentence;
import com.sh.threesentences.topic.entity.SubTopic;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {

    List<Sentence> findBySubTopic(SubTopic subTopic);

    int countByUserIdAndSubTopic(Long userId, SubTopic subTopic);
}
