package com.sh.threesentences.users.dto;

import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailDuplicateCheckDto {

    @Email(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    public EmailDuplicateCheckDto(String email) {
        this.email = email;
    }
}
