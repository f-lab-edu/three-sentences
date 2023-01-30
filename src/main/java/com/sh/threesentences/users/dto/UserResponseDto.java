package com.sh.threesentences.users.dto;

import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.MembershipType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class UserResponseDto {

    private final Long id;

    private final String email;

    private final String name;

    private final MembershipType membership;

    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(user.getId(), user.getEmail(), user.getName(), user.getMembership());
    }

}
