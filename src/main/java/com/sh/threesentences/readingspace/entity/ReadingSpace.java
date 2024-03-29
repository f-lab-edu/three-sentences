package com.sh.threesentences.readingspace.entity;

import com.sh.threesentences.common.entity.BaseEntity;
import com.sh.threesentences.common.enums.OpenType;
import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.topic.entity.Topic;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReadingSpace extends BaseEntity {

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

    /**
     * 스페이스 프로필 이미지
     */
    @Column
    private String profileImageUrl;

    @OneToMany(mappedBy = "readingSpace")
    private final List<ReadingSpaceMemberRole> readingSpaceMemberRole = new ArrayList<>();

    @OneToMany(mappedBy = "readingSpace")
    private final List<Topic> topics = new ArrayList<>();

    @Builder
    public ReadingSpace(Long id, String name, String description, OpenType openType, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.openType = openType;
        this.profileImageUrl = profileImageUrl;
    }

    public void update(ReadingSpaceRequestDto readingSpaceRequestDto) {
        this.name = readingSpaceRequestDto.getName();
        this.description = readingSpaceRequestDto.getDescription();
        this.openType = readingSpaceRequestDto.getOpenType();
        this.profileImageUrl = readingSpaceRequestDto.getProfileImageUrl();
    }

    public void addTopics(Topic topic) {
        topics.add(topic);
    }

    public void addMemberRole(ReadingSpaceMemberRole memberRole) {
        readingSpaceMemberRole.add(memberRole);
    }
}
