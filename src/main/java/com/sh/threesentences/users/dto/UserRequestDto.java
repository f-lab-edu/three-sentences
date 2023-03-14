package com.sh.threesentences.users.dto;

import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.MembershipType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRequestDto {

    @NotBlank(message = "메일을 입력해주세요.")
    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힇]+$", message = "이름은 한글로만 이루어져야 합니다.")
    @Size(max = 20, message = "이름의 최대 길이는 20자 입니다.")
    private String name;

    @NotNull(message = "패스워드를 입력해주세요.")
    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;

    private MembershipType membership;

    public User toEntity(String encryptedPassword) {
        return User.builder()
            .email(this.getEmail())
            .name(this.getName())
            .password(encryptedPassword)
            .membership(this.getMembership())
            .build();
    }
}
