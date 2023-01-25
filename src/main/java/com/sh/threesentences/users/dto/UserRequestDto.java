package com.sh.threesentences.users.dto;

import com.sh.threesentences.users.enums.MembershipType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserRequestDto {

    private String email;

    private String name;

    private String password;

    private MembershipType membership;

}
