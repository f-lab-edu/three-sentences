package com.sh.threesentences.users.dto;

import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.MembershipType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserRequestDto {

    @NotBlank(message= "메일을 입력해주세요.")
    @Email(message= "이메일 형식이 잘못되었습니다.")
    private final String email;

    @NotBlank(message= "이름을 입력해주세요.")
    @Size(max=20, message="이름의 최대 길이는 20자 입니다.")
    private final String name;

    @NotNull(message = "패스워드를 입력해주세요.")
    @NotBlank(message = "패스워드를 입력해주세요.")
    private final String password;

    private final MembershipType membership;

    public User toEntity(String encryptedPassword) {
        return User.builder()
            .email(this.getEmail())
            .name(this.getName())
            .password(encryptedPassword)
            .membership(this.getMembership())
            .build();
    }
}
