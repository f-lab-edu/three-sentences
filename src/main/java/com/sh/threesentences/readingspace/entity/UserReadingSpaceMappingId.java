package com.sh.threesentences.readingspace.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class UserReadingSpaceMappingId implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "reading_space_id")
    Long readingSpaceId;

    public UserReadingSpaceMappingId(Long userId, Long readingSpaceId) {
        this.userId = userId;
        this.readingSpaceId = readingSpaceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserReadingSpaceMappingId that = (UserReadingSpaceMappingId) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(
            getReadingSpaceId(), that.getReadingSpaceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getReadingSpaceId());
    }
}
