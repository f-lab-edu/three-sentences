package com.sh.threesentences.users.dto;

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

}
