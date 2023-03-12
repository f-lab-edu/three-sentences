package com.sh.threesentences.topic.repository;

import com.sh.threesentences.topic.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {


}
