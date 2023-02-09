package com.sh.threesentences.readingspace.entity.fcc;

import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.NO_ADMIN_IN_SPACE;
import static com.sh.threesentences.readingspace.exception.ReadingSpaceErrorCode.READING_SPACE_NOT_FOUND;

import com.sh.threesentences.readingspace.entity.ReadingSpaceMemberRole;
import com.sh.threesentences.readingspace.enums.UserRole;
import java.util.List;

public class ReadingSpaceMembers {

    private final List<ReadingSpaceMemberRole> readingSpaceMembers;

    public ReadingSpaceMembers(List<ReadingSpaceMemberRole> readingSpaceMembers) {
        this.readingSpaceMembers = readingSpaceMembers;
    }

    public void checkSpaceDeleteCondition() {
        if (hasMemberEqualToOne() && memberIsNotAdmin()) {
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

    public static ReadingSpaceMembers fromEntity(List<ReadingSpaceMemberRole> readingSpaceMemberRoles) {
        return new ReadingSpaceMembers(readingSpaceMemberRoles);
    }


}
