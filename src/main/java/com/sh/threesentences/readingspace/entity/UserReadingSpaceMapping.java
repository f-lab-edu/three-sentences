package com.sh.threesentences.readingspace.entity;

import com.sh.threesentences.readingspace.enums.UserRole;
import com.sh.threesentences.users.entity.User;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.Getter;

@Entity
@Getter
public class UserReadingSpaceMapping {


    @EmbeddedId
    UserReadingSpaceMappingKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("readingSpaceId")
    @JoinColumn(name = "readingspace_id")
    private ReadingSpace readingSpace;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;
}
