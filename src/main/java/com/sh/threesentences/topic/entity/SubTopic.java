package com.sh.threesentences.topic.entity;

import com.sh.threesentences.common.entity.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class SubTopic extends BaseEntity {

    /**
     * 엔티티 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * SubTopic 이름
     */
    @Column(unique = true)
    private String name;

    /**
     * 스페이스에 대한 설명
     */
    @Column
    private String description;

    /**
     * 공개 여부
     */
    @Column
    private int startPage;

    @Column
    private int endPage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @OneToMany(mappedBy = "subTopic")
    private final List<Sentence> sentences = new ArrayList<>();

    @Builder
    public SubTopic(Long id, String name, String description, int startPage, int endPage, Topic topic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startPage = startPage;
        this.endPage = endPage;
        this.topic = setTopic(topic);
    }

    private Topic setTopic(Topic topic) {
        if (this.topic != null) {
            this.topic.getSubTopics().remove(this);
        }
        this.topic = topic;
        topic.addSubtopics(this);

        return topic;
    }

    public void addSentences(Sentence sentence) {
        sentences.add(sentence);
    }

}
