package com.sh.threesentences.readingspace.entity.fcc;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.MEMBER_IS_STILL_IN_SPACE;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.NO_ADMIN_IN_SPACE;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;

import com.sh.threesentences.readingspace.entity.UserReadingSpaceMapping;
import com.sh.threesentences.readingspace.enums.UserRole;
import java.util.List;

public class ReadingSpaceMembers {

    private List<UserReadingSpaceMapping> readingSpaceMembers;

    public ReadingSpaceMembers(List<UserReadingSpaceMapping> readingSpaceMembers) {
        this.readingSpaceMembers = readingSpaceMembers;
    }

    public void checkSpaceDeleteCondition() {
        if (hasMembersGreaterThanOne()) {
            throw new IllegalStateException(MEMBER_IS_STILL_IN_SPACE.getMessage());
        } else if (hasMemberEqualToOne() && memberIsNotAdmin()) {
            throw new IllegalStateException(NO_ADMIN_IN_SPACE.getMessage());
        } else if (hasNoMember()) {
            throw new IllegalStateException(READING_SPACE_NOT_FOUND.getMessage());
        }
    }

    private boolean hasNoMember() {
        return readingSpaceMembers.isEmpty();
    }

    private boolean hasMemberEqualToOne() {
        return readingSpaceMembers.size() == 1;
    }

    private boolean memberIsNotAdmin() {
        return readingSpaceMembers.get(0).getUserRole() != UserRole.ADMIN;
    }


    private boolean hasMembersGreaterThanOne() {
        return readingSpaceMembers.size() >= 2;
    }

    public static ReadingSpaceMembers fromEntity(List<UserReadingSpaceMapping> userReadingSpaceMappings) {
        return new ReadingSpaceMembers(userReadingSpaceMappings);
    }


}
