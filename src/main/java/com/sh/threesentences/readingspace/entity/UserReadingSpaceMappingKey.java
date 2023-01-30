package com.sh.threesentences.readingspace.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class UserReadingSpaceMappingKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "readingspace_id")
    Long readingSpaceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserReadingSpaceMappingKey that = (UserReadingSpaceMappingKey) o;
        return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(
            getReadingSpaceId(), that.getReadingSpaceId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId(), getReadingSpaceId());
    }
}
