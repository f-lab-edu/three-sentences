package com.sh.threesentences.topic.entity;

import com.sh.threesentences.common.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {

    /**
     * 엔티티 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 작성자 id
     */
    @Column
    private int userId;

    /**
     * 문장 선정 이유
     */
    @Column
    private String comment;

    /**
     * 좋아요 수
     */
    @Column
    private int likes;

    @ManyToOne
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @Builder
    public Comment(Long id, String name, int userId, String comment, int likes, Sentence sentence) {
        this.id = id;
        this.userId = userId;
        this.comment = comment;
        this.likes = likes;
        this.sentence = sentence;
    }
}
