package com.sh.threesentences.topic.entity;

import com.sh.threesentences.common.entity.BaseEntity;
import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.entity.ReadingSpace;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class Topic extends BaseEntity {

    /**
     * 엔티티 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ReadingSpace 이름
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
    @Enumerated(EnumType.STRING)
    private OpenType openType = OpenType.PUBLIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reading_space_id")
    private ReadingSpace readingSpace;

    @OneToMany(mappedBy = "topic")
    private final List<SubTopic> subTopics = new ArrayList<>();

    @Builder
    public Topic(Long id, String name, String description, OpenType openType, ReadingSpace readingSpace) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.openType = openType;
        this.readingSpace = readingSpace;
    }

}
