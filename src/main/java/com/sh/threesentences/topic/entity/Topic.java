package com.sh.threesentences.topic.entity;

import static com.sh.threesentences.topic.exception.TopicErrorCode.TOPIC_NOT_FOUND;

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
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor
@Where(clause = "is_deleted=false")
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
     * 도서 API 조회를 위한 식별자(ISBN)
     */
    @Column
    private String isbn;

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
    public Topic(Long id, String name, String description, String isbn, OpenType openType,
        ReadingSpace readingSpace) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isbn = isbn;
        this.openType = openType;
        this.readingSpace = setReadingSpace(readingSpace);
    }

    public void checkDelete() {
        if (this.getIsDeleted()) {
            throw new IllegalStateException(TOPIC_NOT_FOUND.getMessage());
        }
    }

    private ReadingSpace setReadingSpace(ReadingSpace readingSpace) {
        if (this.readingSpace != null) {
            this.readingSpace.getTopics().remove(this);
        }
        this.readingSpace = readingSpace;
        readingSpace.addTopics(this);

        return readingSpace;
    }

    public void addSubtopics(SubTopic subTopic) {
        subTopics.add(subTopic);
    }
}
