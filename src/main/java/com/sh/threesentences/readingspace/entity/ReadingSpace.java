package com.sh.threesentences.readingspace.entity;

import com.sh.threesentences.common.entity.BaseEntity;
import com.sh.threesentences.common.enums.OpenType;
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
    private List<UserReadingSpaceMapping> userReadingSpaceMappingList;


    @Builder
    public ReadingSpace(Long id, String name, String description, OpenType openType, String profileImageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.openType = openType;
        this.profileImageUrl = profileImageUrl;
    }

}
