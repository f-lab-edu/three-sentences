package com.sh.threesentences.readingspace.entity;

import com.sh.threesentences.common.entity.BaseEntity;
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
public class ReadingSpaceMemberRole extends BaseEntity {

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
        this.user = setUser(user);
        this.readingSpace = setReadingSpace(readingSpace);
        this.userRole = userRole;
    }

    public boolean isAdmin() {
        return this.getUserRole().equals(UserRole.ADMIN);
    }

    private ReadingSpace setReadingSpace(ReadingSpace readingSpace) {
        if (this.readingSpace != null) {
            this.readingSpace.getReadingSpaceMemberRole().remove(this);
        }
        this.readingSpace = readingSpace;
        readingSpace.addMemberRole(this);

        return readingSpace;
    }

    private User setUser(User user) {
        if (this.user != null) {
            this.user.getReadingSpaceMemberRole().remove(this);
        }
        this.user = user;
        user.addMemberRole(this);

        return user;
    }
}
