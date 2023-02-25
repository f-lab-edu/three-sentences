package com.sh.threesentences.topic.entity;

import com.sh.threesentences.common.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Sentence extends BaseEntity {

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
     * 문장에 대한 페이지
     */
    @Column
    private int page;

    /**
     * 문장
     */
    @Column
    private String sentence;

    /**
     * 문장 선정 이유
     */
    @Column
    private String thoughts;

    /**
     * 좋아요 수
     */
    @Column
    private int likes;

    @ManyToOne
    @JoinColumn(name = "subtopic_id")
    private SubTopic subTopic;

    @OneToMany(mappedBy = "sentence")
    private final List<Comment> comments = new ArrayList<>();

    @Builder
    public Sentence(Long id, String name, Long userId, int page, String sentence, String thoughts, int likes,
        SubTopic subTopic) {
        this.id = id;
        this.userId = userId;
        this.page = page;
        this.sentence = sentence;
        this.thoughts = thoughts;
        this.likes = likes;
        this.subTopic = subTopic;
    }
}
