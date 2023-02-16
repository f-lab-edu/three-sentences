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
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ReadingSpaceMemberRole {

    @EmbeddedId
    UserReadingSpaceMappingId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("readingSpaceId")
    @JoinColumn(name = "reading_space_id")
    private ReadingSpace readingSpace;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public ReadingSpaceMemberRole(User user, ReadingSpace readingSpace, UserRole userRole) {
        this.id = new UserReadingSpaceMappingId(user.getId(), readingSpace.getId());
        this.user = user;
        this.readingSpace = readingSpace;
        this.userRole = userRole;
    }

    public boolean isAdmin() {
        return this.getUserRole().equals(UserRole.ADMIN);
    }

}
