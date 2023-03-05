package com.sh.threesentences.topic.entity;

import com.sh.threesentences.common.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private Long userId;

    /**
     * 문장 선정 이유
     */
    @Column
    private String contents;

    /**
     * 좋아요 수
     */
    @Column
    private int likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

    @Builder
    public Comment(Long id, String name, Long userId, String contents, int likes, Sentence sentence) {
        this.id = id;
        this.userId = userId;
        this.contents = contents;
        this.likes = likes;
        this.sentence = setSentence(sentence);
    }
    
    private Sentence setSentence(Sentence sentence) {
        if (this.sentence != null) {
            this.sentence.getComments().remove(this);
        }
        this.sentence = sentence;
        sentence.addComments(this);

        return sentence;
    }
}
